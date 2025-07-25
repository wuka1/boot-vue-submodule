package com.example.permission.cedar;

import com.cedarpolicy.BasicAuthorizationEngine;
import com.cedarpolicy.model.AuthorizationRequest;
import com.cedarpolicy.model.AuthorizationResponse;
import com.cedarpolicy.model.exception.AuthException;
import com.cedarpolicy.model.slice.BasicSlice;
import com.cedarpolicy.model.slice.Entity;
import com.cedarpolicy.model.slice.Policy;
import com.cedarpolicy.model.slice.Slice;
import com.cedarpolicy.value.CedarMap;
import com.cedarpolicy.value.EntityTypeName;
import com.cedarpolicy.value.EntityUID;
import com.cedarpolicy.value.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//import static com.cedarpolicy.CedarJson.objectWriter;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2024/8/16
 * *@Version 1.0
 **/

@SpringBootTest
public class DemoVerify {
    private static final String TEST_RESOURCES_DIR = "src/test/resources/";
    EntityTypeName principalType;
    EntityTypeName actionType;
    EntityTypeName resourceType;

    private static final String ENTITY_ESCAPE_SEQ = "__entity";


    @Test
    public void jsonToEntity() throws JsonProcessingException {
        var gandalf = new EntityUID(EntityTypeName.parse("Alice").get(), "PayrollApp::Employee");
        var opens = new EntityUID(EntityTypeName.parse("viewSalary").get(), "PayrollApp::Action");
        var moria = new EntityUID(EntityTypeName.parse("Bob").get(), "PayrollApp::Salary");
        AuthorizationRequest q = new AuthorizationRequest(gandalf, opens, moria, new HashMap<String, Value>());

        // 构建json
        ObjectNode n = JsonNodeFactory.instance.objectNode();
//        n.set("context", JsonNodeFactory.instance.objectNode());
//        n.set("schema", JsonNodeFactory.instance.nullNode());
        n.set("principal", buildEuidObject("PayrollApp::Employee", "Alice"));
        n.set("action", buildEuidObject("PayrollApp::Action", "viewSalary"));
        n.set("resource", buildEuidObject("PayrollApp::Salary", "Salary-Bob"));
//        n.set("validateRequest", JsonNodeFactory.instance.booleanNode(false));

        // json转对象
        ObjectMapper mapper = new ObjectMapper();
        AuthorizationRequest authorizationRequest = mapper.convertValue(n, AuthorizationRequest.class);
        System.out.println(authorizationRequest);
//        System.out.println(ar);
//        System.out.println(n);
//        System.out.println("-----------\t" + q);
    }

    private ObjectNode buildEuidObject(String type, String id) {
        var n = JsonNodeFactory.instance.objectNode();
        var inner = JsonNodeFactory.instance.objectNode();
        inner.put("id", id);
        inner.put("type", type);
        n.replace(ENTITY_ESCAPE_SEQ, inner);
        return n;
    }



    @Test
    public void buildEntity() {
        // EntityUID
        EntityUID entityUID = new EntityUID(EntityTypeName.parse("Alice").get(), "PayrollApp::Employee");
        System.out.println("=========\t" +entityUID);

        // attribute
        CedarMap attrs = new CedarMap();
        EntityUID attrUID = new EntityUID(EntityTypeName.parse("Alice").get(), "owner");
        attrs.put("owner", attrUID);
        // parentsUID

        Entity principal = new Entity(entityUID, attrs, new HashSet<>());
        System.out.println("\n---------------------\n" + principal);

        ObjectMapper mapper = new ObjectMapper();

        // 创建一个 CedarMap 实例并添加键值对
        CedarMap cedarMap = new CedarMap();
        cedarMap.put("key1", entityUID);

        // 输出 CedarMap 的 Cedar 表达式
        System.out.println(cedarMap.toCedarExpr());
    }

    /**
     * 可以实现基于属性类的认证
     */
    @Test
    public void conditionDemo() throws IOException, AuthException {
        // 1 构建实体
        //Entity<-EntityUID<-EntityTypeName/EntityIdentifier
        // 1.1 构建实体的EUID
        EntityUID principalUID = new EntityUID(EntityTypeName.parse("Principal").get(), "User::Alice");
        EntityUID actionUID = new EntityUID(EntityTypeName.parse("Action").get(), "viewSalary");
        EntityUID resourceUID = new EntityUID(EntityTypeName.parse("Resource").get(), "/salary");

        // 1.2 构建实体的attrs
//        PrimString attrsValue = new PrimString("Alice"); // attrs Value为string类型
        CedarMap attrsMap = new CedarMap();  // attrs Value为map类型
        attrsMap.put("owner", principalUID);
        //构建实体
        Entity resource = new Entity(resourceUID, attrsMap, new HashSet<>());
        // 实体id--实体属性--实体parentid
        Entity principal = new Entity(principalUID, new HashMap<>(), new HashSet<>());
        Entity action = new Entity(actionUID, new HashMap<>(), new HashSet<>());
        // 构建context
        CedarMap contextsMap = new CedarMap();
        // 2 构建AuthorizationRequest
        AuthorizationRequest r = new AuthorizationRequest(principalUID, actionUID, resource.getEUID(), contextsMap);

        // 3 构建BasicSlice
        // 3.1 构建policy
        var policies = new HashSet<Policy>();
        Policy p = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "condition.cedar")));
        System.out.println("\n policy------" + p);
        policies.add(p);
        // 3.2 构建entities
        Set<Entity> entities = new HashSet<>();
        entities.add(principal);
        entities.add(action);
        entities.add(resource);

        var slice = new BasicSlice(policies, entities);

        // 4 验证
        var auth = new BasicAuthorizationEngine();
        var response = auth.isAuthorized(r, slice);
        System.out.println("\n==========是否通过:==============\n" +response.isAllowed());
    }

    /**
     * 可以实现基于角色、基于组织类的认证
     */
    @Test
    public void groupDemo() throws IOException, AuthException { // in关键字，通过parent来判断
        // 1 构建实体
        //Entity<-EntityUID<-EntityTypeName/EntityIdentifier
        // 1.1 构建实体的EUID
        EntityUID principalUID = new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice");
        System.out.println("\n----------principalUID---------\n" + principalUID);
        EntityUID actionUID = new EntityUID(EntityTypeName.parse("Action").get(), "viewSalary");
        EntityUID resourceUID = new EntityUID(EntityTypeName.parse("Resource").get(), "/list/salary");

        // 1.2 构建实体的attrs
//        PrimString attrsValue = new PrimString("Alice"); // attrs Value为string类型
        CedarMap attrsMap = new CedarMap();  // attrs Value为map类型
//        attrsMap.put("owner", principalUID);
        //构建实体
        Entity resource = new Entity(resourceUID, attrsMap, new HashSet<>());
        // 实体id--实体属性--实体parentid
//        Entity principal = new Entity(principalUID, new HashMap<>(), new HashSet<>());
//        Entity action = new Entity(actionUID, new HashMap<>(), new HashSet<>());
        // 构建context
        CedarMap contextsMap = new CedarMap();
        // 2 构建AuthorizationRequest
        AuthorizationRequest r = new AuthorizationRequest(principalUID, actionUID, resource.getEUID(), contextsMap);
        System.out.println("\n--------AuthorizationRequest---------\n" + r);
        // 3 构建BasicSlice
        // 3.1 构建policy
        var policies = new HashSet<Policy>();
        Policy p = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "group.cedar")));
        System.out.println("\n policy------" + p);
        policies.add(p);
        // 3.2 构建entities
        Set<Entity> entities = new HashSet<>();
        CedarMap attrsMap1 = new CedarMap();
        Set<EntityUID> p1_parents = new HashSet<>();
        EntityUID p1 = new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary");
        EntityUID p2 = new EntityUID(EntityTypeName.parse("Role").get(), "editSalary");
        p1_parents.add(p1);
        p1_parents.add(p2);
        Set<EntityUID> p2_parents = new HashSet<>();
        Entity e1 = new Entity(new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice"), attrsMap1,
                p1_parents);
        Entity e2 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary"), attrsMap1, p2_parents);
        Entity e3 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "editSalary"), attrsMap1, p2_parents);
        entities.add(e1);
        entities.add(e2);
        entities.add(e3);
        var slice = new BasicSlice(policies, entities);

        // 4 验证
        var auth = new BasicAuthorizationEngine();
        var response = auth.isAuthorized(r, slice);
        System.out.println("\n==========是否通过:==============\n" +response.isAllowed());
    }


    /**
     *  实现上下文级别的认证：例如时间、ip
     */
    @Test
    public void contextIpDemo() throws IOException, AuthException { // in关键字，通过parent来判断
        // 1 构建实体
        //Entity<-EntityUID<-EntityTypeName/EntityIdentifier
        // 1.1 构建实体的EUID
        EntityUID principalUID = new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice");
        System.out.println("\n----------principalUID---------\n" + principalUID);
        EntityUID actionUID = new EntityUID(EntityTypeName.parse("Action").get(), "viewSalary");
        EntityUID resourceUID = new EntityUID(EntityTypeName.parse("Resource").get(), "/list/salary");

        // 1.2 构建实体的attrs
//        PrimString attrsValue = new PrimString("Alice"); // attrs Value为string类型
        CedarMap attrsMap = new CedarMap();  // attrs Value为map类型
//        attrsMap.put("owner", principalUID);
        //构建实体
        Entity resource = new Entity(resourceUID, attrsMap, new HashSet<>());
        // 实体id--实体属性--实体parentid
        Entity principal = new Entity(principalUID, new HashMap<>(), new HashSet<>());
        Entity action = new Entity(actionUID, new HashMap<>(), new HashSet<>());
        // 构建context-------------------------------------------------------------------------
        CedarMap contextsMap = new CedarMap();
        PrimString contextValue = new PrimString("192.168.0.1"); // attrs Value为string类型
        contextsMap.put("ip", contextValue);
        // 2 构建AuthorizationRequest
        AuthorizationRequest r = new AuthorizationRequest(principalUID, actionUID, resource.getEUID(), contextsMap);
        System.out.println("\n--------AuthorizationRequest---------\n" + r);
        // 3 构建BasicSlice
        // 3.1 构建policy
        var policies = new HashSet<Policy>();
        Policy p = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "context_ip.cedar")));
        System.out.println("\n policy------" + p);
        policies.add(p);
        // 3.2 构建entities
        Set<Entity> entities = new HashSet<>();
        CedarMap attrsMap1 = new CedarMap();
        Set<EntityUID> p1_parents = new HashSet<>();
        EntityUID p1 = new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary");
        EntityUID p2 = new EntityUID(EntityTypeName.parse("Role").get(), "editSalary");
        p1_parents.add(p1);
        p1_parents.add(p2);
        Set<EntityUID> p2_parents = new HashSet<>();
        Entity e1 = new Entity(new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice"), attrsMap1,
                p1_parents);
        Entity e2 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary"), attrsMap1, p2_parents);
        Entity e3 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "editSalary"), attrsMap1, p2_parents);
        entities.add(e1);
        entities.add(e2);
        entities.add(e3);
        var slice = new BasicSlice(policies, entities);

        // 4 验证
        var auth = new BasicAuthorizationEngine();
        var response = auth.isAuthorized(r, slice);
        System.out.println("\n==========是否通过:==============\n" +response.isAllowed());
    }

    /**
     *  实现上下文级别的认证：例如时间、ip
     */
    @Test
    public void contextTimeDemo() throws IOException, AuthException { // in关键字，通过parent来判断
        // 访问起止时间
        LocalDateTime startTime =  LocalDateTime.parse("2024-08-20T16:30:00");
        LocalDateTime endTime =  LocalDateTime.parse("2024-08-20T16:35:00");

        // 1 构建实体
        //Entity<-EntityUID<-EntityTypeName/EntityIdentifier
        // 1.1 构建实体的EUID
        EntityUID principalUID = new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice");
        System.out.println("\n----------principalUID---------\n" + principalUID);
        EntityUID actionUID = new EntityUID(EntityTypeName.parse("Action").get(), "viewSalary");
        EntityUID resourceUID = new EntityUID(EntityTypeName.parse("Resource").get(), "/list/salary");

        // 1.2 构建实体的attrs
//        PrimString attrsValue = new PrimString("Alice"); // attrs Value为string类型
        CedarMap attrsMap = new CedarMap();  // attrs Value为map类型
        PrimLong startValue = new PrimLong(startTime.toEpochSecond(ZoneOffset.UTC));
        PrimLong endValue = new PrimLong(endTime.toEpochSecond(ZoneOffset.UTC)); //
        attrsMap.put("startTime", startValue);
        attrsMap.put("endTime", endValue);
        //构建实体
        Entity resource = new Entity(resourceUID, attrsMap, new HashSet<>()); // 会绑定新增的规则
//        // 实体id--实体属性--实体parentid
//        Entity principal = new Entity(principalUID, new HashMap<>(), new HashSet<>());
//        Entity action = new Entity(actionUID, new HashMap<>(), new HashSet<>());
        // 构建context-------------------------------------------------------------------------
        CedarMap contextsMap = new CedarMap();
//        PrimLong startValue = new PrimLong(startTime.toEpochSecond(ZoneOffset.UTC));
        // attrs Value为string类型
//        PrimLong endValue = new PrimLong(endTime.toEpochSecond(ZoneOffset.UTC)); //
        // attrs Value为string类型
        // contextsMap.put("startTime", startValue);
//        contextsMap.put("endTime", endValue);
        PrimLong now = new PrimLong(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        contextsMap.put("now", now); // 可以默认都带有这个参数

        // 2 构建AuthorizationRequest
        AuthorizationRequest r = new AuthorizationRequest(principalUID, actionUID, resource.getEUID(), contextsMap);

        // 3 构建BasicSlice
        // 3.1 构建policy
        var policies = new HashSet<Policy>();
        Policy p = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "context_time.cedar")));
        System.out.println("\n policy------" + p);
        policies.add(p);
        // 3.2 构建entities
        Set<Entity> entities = new HashSet<>();
        CedarMap attrsMap1 = new CedarMap();
        Set<EntityUID> p1_parents = new HashSet<>();
        EntityUID p1 = new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary");
        EntityUID p2 = new EntityUID(EntityTypeName.parse("Role").get(), "editSalary");
        p1_parents.add(p1);
        p1_parents.add(p2);
        Set<EntityUID> p2_parents = new HashSet<>();
        Entity e1 = new Entity(new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice"), attrsMap1,
                p1_parents);
        Entity e2 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary"), attrsMap1, p2_parents);
        Entity e3 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "editSalary"), attrsMap1, p2_parents);
        entities.add(e1);
        entities.add(e2);
        entities.add(e3);
        entities.add(resource);
        var slice = new BasicSlice(policies, entities);

        // 4 验证
        var auth = new BasicAuthorizationEngine();
        var response = auth.isAuthorized(r, slice);
        System.out.println("\n==========是否通过:==============\n" +response.isAllowed());
    }

    @Test
    public void combinationOrDemo() throws IOException, AuthException { // in关键字，通过parent来判断
        // 访问起止时间
        LocalDateTime startTime =  LocalDateTime.parse("2024-08-20T16:30:00");
        LocalDateTime endTime =  LocalDateTime.parse("2024-08-20T17:04:00");

        // 1 构建实体
        //Entity<-EntityUID<-EntityTypeName/EntityIdentifier
        // 1.1 构建实体的EUID
        EntityUID principalUID = new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice");
        System.out.println("\n----------principalUID---------\n" + principalUID);
        EntityUID actionUID = new EntityUID(EntityTypeName.parse("Action").get(), "viewSalary");
        EntityUID resourceUID = new EntityUID(EntityTypeName.parse("Resource").get(), "/list/salary");

        // 1.2 构建实体的attrs
//        PrimString attrsValue = new PrimString("Alice"); // attrs Value为string类型
        CedarMap attrsMap = new CedarMap();  // attrs Value为map类型
        CedarMap contentMap = new CedarMap();
        PrimLong startValue = new PrimLong(startTime.toEpochSecond(ZoneOffset.UTC));
        PrimLong endValue = new PrimLong(endTime.toEpochSecond(ZoneOffset.UTC)); //
        contentMap.put("startTime", startValue);
        contentMap.put("endTime", endValue);
        attrsMap.put("timePolicy", contentMap);
        System.out.println("-----------attrsMap----------\n" + attrsMap.get("timePolicy").toCedarExpr());
        //构建实体
        Entity resource = new Entity(resourceUID, attrsMap, new HashSet<>()); // 会绑定新增的规则
        System.out.println("-------------resource:----------\n" + resource);

        // 构建context-------------------------------------------------------------------------
        CedarMap contextsMap = new CedarMap();
        PrimLong now = new PrimLong(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        contextsMap.put("now", now); // 可以默认都带有这个参数

        // 2 构建AuthorizationRequest
        AuthorizationRequest r = new AuthorizationRequest(principalUID, actionUID, resource.getEUID(), contextsMap);

        // 3 构建BasicSlice
        // 3.1 构建policy
        var policies = new HashSet<Policy>();
        Policy p1 = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "context_time.cedar")));
        Policy p2 = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "group.cedar")));
        System.out.println("\n policy------" + p1 + "--------\n" + p2);
        policies.add(p1);
        policies.add(p2);  //多个policy callCedarJNI默认采用或运算，要实现与运算，则通过多个slice结果进行逻辑运算

        // 3.2 构建entities--入参
        Set<Entity> entities = new HashSet<>();
        CedarMap attrsMap1 = new CedarMap();
        Set<EntityUID> p1_parents = new HashSet<>();
        EntityUID eUID1 = new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary");
        EntityUID eUID2 = new EntityUID(EntityTypeName.parse("Role").get(), "editSalary");
        p1_parents.add(eUID1);
        p1_parents.add(eUID2);
        Set<EntityUID> p2_parents = new HashSet<>();
        Entity e1 = new Entity(new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice"), attrsMap1,
                p1_parents);
        Entity e2 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary"), attrsMap1, p2_parents);
        Entity e3 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "editSalary"), attrsMap1, p2_parents);
        entities.add(e1);
        entities.add(e2);
        entities.add(e3);
        entities.add(resource);  // resource 绑定了新的规则
        var slice = new BasicSlice(policies, entities);

        var policies2 = new HashSet<Policy>();
        policies2.add(p2);
        var slice2 = new BasicSlice(policies2, entities);

        // 4 验证
        var auth = new BasicAuthorizationEngine();
        var response = auth.isAuthorized(r, slice);
        System.out.println("\n==========是否通过:==============\n" + response.isAllowed());
    }

    /**
     *  策略组合类demo
     */
    @Test
    public void combinationAndDemo() throws IOException, AuthException { // in关键字，通过parent来判断
        // 访问起止时间
        LocalDateTime startTime =  LocalDateTime.parse("2024-08-20T16:30:00");
        LocalDateTime endTime =  LocalDateTime.parse("2024-08-20T17:04:00");

        // 1 构建实体
        //Entity<-EntityUID<-EntityTypeName/EntityIdentifier
        // 1.1 构建实体的EUID
        EntityUID principalUID = new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice");
        System.out.println("\n----------principalUID---------\n" + principalUID);
        EntityUID actionUID = new EntityUID(EntityTypeName.parse("Action").get(), "viewSalary");
        EntityUID resourceUID = new EntityUID(EntityTypeName.parse("Resource").get(), "/list/salary");

        // 1.2 构建实体的attrs
//        PrimString attrsValue = new PrimString("Alice"); // attrs Value为string类型
        CedarMap attrsMap = new CedarMap();  // attrs Value为map类型
        CedarMap contentMap = new CedarMap();
        PrimLong startValue = new PrimLong(startTime.toEpochSecond(ZoneOffset.UTC));
        PrimLong endValue = new PrimLong(endTime.toEpochSecond(ZoneOffset.UTC)); //
        contentMap.put("startTime", startValue);
        contentMap.put("endTime", endValue);
        attrsMap.put("timePolicy", contentMap);
        System.out.println("-----------attrsMap----------\n" + attrsMap.get("timePolicy").toCedarExpr());
        //构建实体
        Entity resource = new Entity(resourceUID, attrsMap, new HashSet<>()); // 会绑定新增的规则
        System.out.println("-------------resource:----------\n" + resource);

        // 构建context-------------------------------------------------------------------------
        CedarMap contextsMap = new CedarMap();
        PrimLong now = new PrimLong(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        contextsMap.put("now", now); // 可以默认都带有这个参数

        // 2 构建AuthorizationRequest
        AuthorizationRequest r = new AuthorizationRequest(principalUID, actionUID, resource.getEUID(), contextsMap);

        // 3 构建BasicSlice
        // 3.1 构建policy
        var policies = new HashSet<Policy>();
        Policy p1 = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "context_time.cedar")));
        Policy p2 = Policy.parseStaticPolicy(Files.readString(
                Path.of(TEST_RESOURCES_DIR + "group.cedar")));
        System.out.println("\n policy------" + p1 + "--------\n" + p2);
        policies.add(p1);
//        policies.add(p2);  //多个policy callCedarJNI默认采用或运算，要实现与运算，则通过多个slice结果进行逻辑运算

        // 3.2 构建entities--入参
        Set<Entity> entities = new HashSet<>();
        CedarMap attrsMap1 = new CedarMap();
        Set<EntityUID> p1_parents = new HashSet<>();
        EntityUID eUID1 = new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary");
        EntityUID eUID2 = new EntityUID(EntityTypeName.parse("Role").get(), "editSalary");
        p1_parents.add(eUID1);
        p1_parents.add(eUID2);
        Set<EntityUID> p2_parents = new HashSet<>();
        Entity e1 = new Entity(new EntityUID(EntityTypeName.parse("Principal::User").get(), "Alice"), attrsMap1,
                p1_parents);
        Entity e2 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "viewSalary"), attrsMap1, p2_parents);
        Entity e3 = new Entity(new EntityUID(EntityTypeName.parse("Role").get(), "editSalary"), attrsMap1, p2_parents);
        entities.add(e1);
        entities.add(e2);
        entities.add(e3);
        entities.add(resource);  // resource 绑定了新的规则
        Set<Slice> slices = new HashSet<>();
        var slice = new BasicSlice(policies, entities);
        slices.add(slice);

        var policies2 = new HashSet<Policy>();
        policies2.add(p2);
        var slice2 = new BasicSlice(policies2, entities);
        slices.add(slice2);

        // 4 验证
        var auth = new BasicAuthorizationEngine();
        boolean res=true;
        for (Slice c: slices) {
            var response = auth.isAuthorized(r, c);
            res = res && response.isAllowed();
        }
        System.out.println("\n==========是否通过:==============\n" + res);
    }



    /**
     * 可以支持哪些场景的验证，方法？资源？
     */
}
