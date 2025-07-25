package com.example.permission.cedar;

import com.cedarpolicy.model.exception.InternalException;
import com.cedarpolicy.model.schema.Schema;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2024/8/16
 * *@Version 1.0
 **/
public class SchemaTest {
    @Test
    public void parseJsonSchema() throws IOException, InternalException {
        // `commonTypes`, `entityTypes`, `actions`是固定的
        Schema.parse("{}");
        Schema.parse("{\"entityName\": {\"entityTypes\": {}, \"actions\": {}}}");
        Schema.parse("{\"ns1\": {\"entityTypes\": {}, \"actions\":  {}}}");

        //Schema(JsonNode)
        String json = "{ \"f1\" : \"v1\" } ";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Schema s = new Schema(jsonNode);
        System.out.println(s);

    }
}