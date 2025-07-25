package com.example.permission.cedar;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.BasicAuthorizationEngine;
import com.cedarpolicy.model.AuthorizationRequest;
import com.cedarpolicy.model.AuthorizationResponse;
import com.cedarpolicy.model.exception.AuthException;
import com.cedarpolicy.model.slice.BasicSlice;
import com.cedarpolicy.model.slice.Entity;
import com.cedarpolicy.model.slice.Policy;
import com.cedarpolicy.value.EntityTypeName;
import com.cedarpolicy.value.EntityUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2024/8/15
 * *@Version 1.0
 **/
@SpringBootTest
public class BasicTest {

    @BeforeAll
    public static void loadDll() {
        System.load(System.getenv("CEDAR_JAVA_FFI_LIB"));
    }

    @Test
    public void simple() throws AuthException {
        var auth = new BasicAuthorizationEngine();
        var principalEUID = new EntityUID(EntityTypeName.parse("User").get(), "alice");
        var actionEUID = new EntityUID(EntityTypeName.parse("Action").get(), "view");
        var q = new AuthorizationRequest(principalEUID, actionEUID, principalEUID, new HashMap<>());
        var policies = new HashSet<Policy>();
        // p0待办规则名称
        policies.add(new Policy("permit(principal,action,resource);", "p0"));
        var slice = new BasicSlice(policies, new HashSet<>());
        assertDoesNotThrow(() -> {
            var response = auth.isAuthorized(q, slice);
            assertTrue(response.isAllowed());
        }, "Should not throw AuthException");
    }


    @Test
    public void first() throws AuthException {
        // policy define
        Set<Policy> ps = new HashSet<>(); //支持多个规则，规则之间是or还是and时如何处理？
        String fullPolicy = "permit(principal == User::\"neal\", action == Action::\"viewPhoto\", resource == Photo::\"photo.jpg\");";
        ps.add(new Policy(fullPolicy, "p1"));
//        ps.add(new Policy("permit(principal,action,resource);", "p0"));  //没有配具体的，代表没有认证


        // data define
//        Set<Entity> e = new HashSet<>();
//        e.add(new Entity(new EntityUID(EntityTypeName.parse("User").get(), "neal"), new HashMap<>(), new HashSet<>()));
//        e.add(new Entity(new EntityUID(EntityTypeName.parse("Action").get(), "viewPhoto"), new HashMap<>(),
//                new HashSet<>()));
//
//        Entity photo = new Entity(new EntityUID(EntityTypeName.parse("Photo").get(),"photo"), new HashMap<>(),
//                new HashSet<>());
//        e.add(photo);

       // 认证请求封装
//        EntityUID user = new EntityUID(EntityTypeName.parse("User").get(), "neal"); // 这里可以满足角色的需求，对应者组织如何处理？
        EntityUID user = new EntityUID(EntityTypeName.parse("User").get(), ""); // 用户为空时，验证入参不完整是如何处理？

        EntityUID action = new EntityUID(EntityTypeName.parse("Action").get(), "viewPhoto"); //多个action如何处理？如view and
        // edit
        EntityUID resource = new EntityUID(EntityTypeName.parse("Photo").get(), "photo11.jpg"); //多个资源时如何封装？
        AuthorizationRequest r = new AuthorizationRequest(user , action, resource, new HashMap<>());

        // verify
        AuthorizationEngine ae = new BasicAuthorizationEngine();
        var slice = new BasicSlice(ps, new HashSet<>());
        AuthorizationResponse resp = ae.isAuthorized(r, slice);
        System.out.println(resp.isAllowed());
    }

    @Test
    public void org() throws AuthException {
        // policy define
        Set<Policy> ps = new HashSet<>(); //支持多个规则，规则之间是or还是and时如何处理？
        String fullPolicy = "permit(principal == cib::ph::kj, action == Action::\"viewPhoto\", resource == " +
                "Photo::\"photo.jpg\");";
        ps.add(new Policy(fullPolicy, "p1"));
//        ps.add(new Policy("permit(principal,action,resource);", "p0"));  //没有配具体的，代表没有认证


        // data define
        Set<Entity> e = new HashSet<>();
        e.add(new Entity(new EntityUID(EntityTypeName.parse("User").get(), "neal"), new HashMap<>(), new HashSet<>()));
        e.add(new Entity(new EntityUID(EntityTypeName.parse("Action").get(), "viewPhoto"), new HashMap<>(),
                new HashSet<>()));

        Entity photo = new Entity(new EntityUID(EntityTypeName.parse("Photo").get(),"photo"), new HashMap<>(),
                new HashSet<>());
        e.add(photo);

        // 认证请求封装
//        EntityUID user = new EntityUID(EntityTypeName.parse("User").get(), "neal"); // 这里可以满足角色的需求，对应者组织如何处理？
        EntityUID user = new EntityUID(EntityTypeName.parse("User").get(), "kj"); // 用户为空时，验证入参不完整是如何处理？

        EntityUID action = new EntityUID(EntityTypeName.parse("Action").get(), "viewPhoto"); //多个action如何处理？如view and
        // edit
        EntityUID resource = new EntityUID(EntityTypeName.parse("Photo").get(), "photo11.jpg"); //多个资源时如何封装？
        AuthorizationRequest r = new AuthorizationRequest(user , action, resource, new HashMap<>());

        // verify
        AuthorizationEngine ae = new BasicAuthorizationEngine();
        var slice = new BasicSlice(ps, new HashSet<>());
        AuthorizationResponse resp = ae.isAuthorized(r, slice);
        System.out.println(resp.isAllowed());
    }

}
