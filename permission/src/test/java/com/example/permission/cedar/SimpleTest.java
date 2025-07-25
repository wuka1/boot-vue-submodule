package com.example.permission;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2024/8/15
 * *@Version 1.0
 **/

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import com.cedarpolicy.model.exception.AuthException;
import com.cedarpolicy.model.AuthorizationResponse;

import com.example.permission.cedar.SampleJavaClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CedarSimpleTest {

    @BeforeAll
    public static void loadDll() {
        System.load(System.getenv("CEDAR_JAVA_FFI_LIB"));
    }

    @Test
    public void sampleMethodTest() {
        SampleJavaClass sampleClass = new SampleJavaClass();
        try {
            assertEquals(true, sampleClass.sampleMethod());
        } catch (AuthException e) {
            fail("Auth Exception: " + e.toString());
        }
    }

    @Test
    public void testFailing() {
        SampleJavaClass sampleClass = new SampleJavaClass();
        try {
            sampleClass.shouldFail();
        } catch (AuthException e) {
            assertEquals("Auth Exception: Bad request: couldn't parse policy with id `p2`\nunexpected token `a`: ", e.toString());
        }
    }

}
