package com.example.permission.cedar;

import com.cedarpolicy.model.exception.InternalException;
import com.cedarpolicy.model.slice.Policy;
import com.cedarpolicy.value.EntityUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2024/8/16
 * *@Version 1.0
 **/

@SpringBootTest
public class PolicyTests {

    private static final String TEST_RESOURCES_DIR = "src/test/resources/";

    @Test
    public void parse() throws InternalException, IOException{
    }

    @Test
    public void formatter() throws IOException, InternalException {
        String unformattedCedarPolicy = Files.readString(
                Path.of(TEST_RESOURCES_DIR + "unformatted_policy.cedar"));

        String formattedCedarPolicy = Files.readString(
                Path.of(TEST_RESOURCES_DIR + "formatted_policy.cedar"));
        Policy p = Policy.parseStaticPolicy(formattedCedarPolicy);// 单个policy配置
        System.out.println("//单个Policy配置:" +p);

        //当前版本静态不支持解析多个policies
//        String multiPolicy = Files.readString(
//                Path.of(TEST_RESOURCES_DIR + "policy.cedar"));
//        Policy mp = Policy.parseStaticPolicy(multiPolicy); //多个policy配置解析
//        System.out.println(mp);

    }

    //entity的处理

    @Test
    public void groupTest() {
        // 规则
        Set<Policy> ps = new HashSet<>(); //支持多个规则，规则之间是or还是and时如何处理？
        String fullPolicy = "permit(principal in Group::\"cib\" , action, resource)";
//        resource has owner && resource.owner == principal //基于属性的demo
        ps.add(new Policy(fullPolicy, "p1"));
        //入参

        //校验

    }

    @Test
    public void attributeTest() {
        // 规则
        Set<Policy> ps = new HashSet<>(); //支持多个规则，规则之间是or还是and时如何处理？
        String fullPolicy = "permit(principal, action, resource) when { principal has x && principal.x == 5}";
//        resource has owner && resource.owner == principal //基于属性的demo
        //可以基于时间？
//        when { context.time < ...}";
        ps.add(new Policy(fullPolicy, "p1"));
    }

    @Test
    public void conditionTest() {
        //时间？

    }


    @Test
    public void namespaceTest() {

    }

    @Test
    public void parseStaticPolicyTests() {
        assertDoesNotThrow(() -> {
            var policy1 = Policy.parseStaticPolicy("permit(principal, action, resource);");
            var policy2 = Policy.parseStaticPolicy("permit(principal, action, resource) when { principal has x && principal.x == 5};");
            assertNotEquals(policy1.policyID.equals(policy2.policyID), true);
        });
        assertThrows(InternalException.class, () -> {
            Policy.parseStaticPolicy("permit();");
        });
        assertThrows(NullPointerException.class, () -> {
            Policy.parseStaticPolicy(null);
        });
    }

    @Test
    public void parsePolicyTemplateTests() {
        assertDoesNotThrow(() -> {
            String tbody = "permit(principal == ?principal, action, resource in ?resource);"; //Policy templates
            var template = Policy.parsePolicyTemplate(tbody);
            assertTrue(template.policySrc.equals(tbody));
        });
        assertThrows(InternalException.class, () -> {
            Policy.parsePolicyTemplate("permit(principal in ?resource, action, resource);");
        });
    }

    @Test
    public void validateTemplateLinkedPolicySuccessTest() { //Policy templates
        Policy p = new Policy("permit(principal == ?principal, action, resource in ?resource);", null);
        EntityUID principal1 = EntityUID.parse("Library::User::\"Victor\"").get();
        EntityUID resource1 = EntityUID.parse("Library::Book::\"The black Swan\"").get();

        Policy p2 = new Policy("permit(principal, action, resource in ?resource);", null);
        EntityUID resource2 = EntityUID.parse("Library::Book::\"Thinking Fast and Slow\"").get();

        Policy p3 = new Policy("permit(principal == ?principal, action, resource);", null);

        Policy p4 = new Policy("permit(principal, action, resource);", null);

        assertDoesNotThrow(() -> {
            assertTrue(Policy.validateTemplateLinkedPolicy(p, principal1, resource1));
            assertTrue(Policy.validateTemplateLinkedPolicy(p2, null, resource2));
            assertTrue(Policy.validateTemplateLinkedPolicy(p3, principal1, null));
            assertTrue(Policy.validateTemplateLinkedPolicy(p4, null, null));
        });
    }
    @Test
    public void validateTemplateLinkedPolicyFailsWhenExpected() {
        Policy p1 = new Policy("permit(principal, action, resource);", null);
        EntityUID principal = EntityUID.parse("Library::User::\"Victor\"").get();
        EntityUID resource = EntityUID.parse("Library::Book::\"Thinking Fast and Slow\"").get();

        Policy p2 = new Policy("permit(principal, action, resource in ?resource);", null);


        Policy p3 = new Policy("permit(principal == ?principal, action, resource);", null);

        // fails if we fill either slot in a policy with no slots
        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p1, principal, null);
        });
        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p1, null, resource);
        });
        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p1, null, resource);
        });
        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p1, principal, resource);
        });


        // fails if we fill both slots or the wrong slot in a policy with one slot
        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p2, principal, null);
        });
        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p2, principal, resource);
        });

        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p3, null, resource);
        });
        assertThrows(InternalException.class, () -> {
            Policy.validateTemplateLinkedPolicy(p3, principal, resource);
        });
    }
}
