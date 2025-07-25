package com.example.permission.cedar;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.BasicAuthorizationEngine;
import com.cedarpolicy.model.AuthorizationRequest;
import com.cedarpolicy.model.AuthorizationResponse;
import com.cedarpolicy.model.AuthorizationResponse.Decision;
import com.cedarpolicy.model.exception.AuthException;
import com.cedarpolicy.model.exception.InternalException;
import com.cedarpolicy.model.schema.Schema;
import com.cedarpolicy.model.slice.BasicSlice;
import com.cedarpolicy.model.slice.Entity;
import com.cedarpolicy.model.slice.Policy;
import com.cedarpolicy.model.slice.Slice;
import com.cedarpolicy.serializer.JsonEUID;
import com.cedarpolicy.value.EntityUID;
import com.cedarpolicy.value.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2024/8/20
 * *@Version 1.0
 **/

@SpringBootTest
public class JsonVerify {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TEST_RESOURCES_DIR = "src/test/resources/";


    @SuppressWarnings("visibilitymodifier")
    private static class JsonTest {
        /**
         * File name of the file containing policies. Path is relative to the integration tests
         * root.
         */
        public String policies;

        /**
         * File name of the file containing entities. Path is relative to the integration tests
         * root.
         */
        public String entities;

        /**
         * File name of the schema file. Path is relative to the integration tests root. Note: This
         * field is currently unused by these tests. The tests should be updated to take advantage
         * of it once there is a Java interface to the validator.
         */
        public String schema;

        /**
         * Whether the given policies are expected to pass the validator with this schema, or not
         */
        public boolean shouldValidate;

        /** List of requests with their expected result. */
        public List<JsonRequest> requests;
    }

    @SuppressWarnings("visibilitymodifier")
    private static class JsonRequest {
        /** Textual description of the request. */
        public String description;

        /** Principal entity uid used for the request. */
        public JsonEUID principal;

        /** Action entity uid used for the request. */
        public JsonEUID action;

        /** Resource entity uid used for the request. */
        public JsonEUID resource;

        /** Context map used for the request. */
        public Map<String, Value> context;

        /** Whether to enable request validation for this request. Default true */
        public boolean validateRequest = true;

        /** The expected decision that should be returned by the authorization engine. */
        public Decision decision;

        /** The expected reason list that should be returned by the authorization engine. */
        public List<String> reason;

        /** The expected error list that should be returned by the authorization engine. */
        public List<String> errors;
    }

    @SuppressWarnings("visibilitymodifier")
    private static class JsonEntity {
        /** Entity uid for the entity. */
        public JsonEUID uid;

        /** Entity attributes, where the value string is a Cedar literal value. */
        public Map<String, Value> attrs;

        /** List of direct parent entities of this entity. */
        public List<JsonEUID> parents;
    }

    private InputStream loadFile(String file) throws IOException {
       InputStream entitiesIn =
                     new FileInputStream(Path.of(TEST_RESOURCES_DIR + file).toFile());
            return entitiesIn;
    }

    private Set<Entity> loadEntities(String entitiesFile) throws IOException {
//        try (InputStream entitiesIn =
//                     new FileInputStream(Path.of(TEST_RESOURCES_DIR + entitiesFile).toFile())) {
//            return Arrays.stream(OBJECT_MAPPER.reader().readValue(entitiesIn, JsonEntity[].class))
//                    .map(je -> loadEntity(je))
//                    .collect(Collectors.toSet());
//        }
        return Arrays.stream(OBJECT_MAPPER.reader().readValue(this.loadFile(entitiesFile), JsonEntity[].class))
                    .map(je -> loadEntity(je))
                    .collect(Collectors.toSet());
    }

    private Policy loadPolice(String policyFile) throws IOException, InternalException {
        Policy p = Policy.parseStaticPolicy(Files.readString(Path.of(TEST_RESOURCES_DIR +policyFile)));
        return p;
    }


    private Entity loadEntity(JsonEntity je) {
        Set<EntityUID> parents = je.parents
                .stream()
                .map(euid -> EntityUID.parseFromJson(euid).get())
                .collect(Collectors.toSet());

        return new Entity(EntityUID.parseFromJson(je.uid).get(), je.attrs, parents);
    }


    private void executeJsonRequestTest(
            Set<Entity> entities, Set<Policy> policySet, JsonRequest request) throws AuthException {
        AuthorizationEngine auth = new BasicAuthorizationEngine();
        AuthorizationRequest authRequest =
                new AuthorizationRequest(
                        EntityUID.parseFromJson(request.principal).get(),
                        EntityUID.parseFromJson(request.action).get(),
                        EntityUID.parseFromJson(request.resource).get(),
                        request.context);

        var slice = new BasicSlice(policySet, entities);
        final AuthorizationResponse response = auth.isAuthorized(authRequest, slice);
        System.out.println("------------执行结果:---------------\n" + response.isAllowed());

    }

    @Test
    public void loadEntitiesTest() throws IOException {
        Set<Entity> entities = this.loadEntities("sample-data/sandbox_a/entities.json");
        for (Entity e: entities) {
            System.out.println("-----------Entity实体：----------------\n" +e);
        }
    }


    @Test
    public void jsonTest() throws IOException, AuthException {
        JsonTest test;
        test = OBJECT_MAPPER.reader().readValue(this.loadFile("tests/example_use_cases/1a.json"), JsonTest.class);
        Set<Entity> entities = loadEntities(test.entities);
        Set<Policy> policies = new HashSet<>();
        Policy policy = loadPolice(test.policies);
        System.out.println("-----------策略-------------\n" + policy);
        policies.add(policy);
        this.executeJsonRequestTest(entities, policies, test.requests.get(0));
    }
}
