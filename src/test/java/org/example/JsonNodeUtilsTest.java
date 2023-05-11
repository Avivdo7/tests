package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonNodeUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Test
    public void testAddAStringFieldRootLevel() {

        String json = "{\"name\":\"Aviv\"}";
        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNodeUtils.addAStringFieldRootLevel(jsonNode, "employed", "false");

        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("name", "Aviv");
        expectedNode.put("employed", "false");

        assertEquals(expectedNode, jsonNode);
    }

    @SneakyThrows
    @Test
    public void testAddAIntFieldRootLevel() {

        String json = "{\"name\":\"Aviv\"}";
        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNodeUtils.addAStringFieldRootLevel(jsonNode, "age", 29);

        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("name", "Aviv");
        expectedNode.put("age", 29);

        assertEquals(expectedNode, jsonNode);
    }

    @SneakyThrows
    @Test
    void testAddABooleanFieldRootLevel() {
        String json = "{\"name\":\"Aviv\"}";
        JsonNode jsonNode = objectMapper.readTree(json);

        JsonNodeUtils.addABooleanFieldRootLevel(jsonNode, "male", true);
        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("name", "Aviv");
        expectedNode.put("male", true);

        assertEquals(expectedNode, jsonNode);
    }

    @Test
    public void testRemoveAFieldRootLevel() throws Exception {

        String jsonString = "{\"name\":\"Aviv\", \"age\":29}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // remove key-value pair
        String key = "age";
        JsonNodeUtils.removeAFieldRootLevel(jsonNode, key);

        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("name", "Aviv");
        assertEquals(expectedNode, jsonNode);

        //try to remove a key that doesn't exist
        String notValidKey = "height";
        JsonNodeUtils.removeAFieldRootLevel(jsonNode, notValidKey);

        //check if the jsonNode has changed with the false removal
        assertEquals(expectedNode, jsonNode);

        // check if the removed key is still in the json object -> assert null
        JsonNode removedValue = jsonNode.get(key);
        assertNull(removedValue);
    }

    @Test
    public void testGetStringFieldValueByPath() throws JsonProcessingException {
        //create
        String jsonString = "{\"name\":{\"first\":\"Aviv\", \"last\":\"Doron\"}, \"age\":29}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // get the nested field and check if its equal
        String[] pathFields = {"name", "first"};
        String expectedValue = "Aviv";
        String actualValue = JsonNodeUtils.getStringFieldValueByPath(jsonNode, pathFields);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetSListFieldValueByPath() throws Exception {
        //create json
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        arrayNode.add("alice");
        arrayNode.add("bob");
        arrayNode.add("charlie");

        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
        objectNode.set("names", arrayNode);

        // check if fields are in json
        String[] pathFields = {"names"};
        List<String> expectedValue = List.of("alice", "bob", "charlie");
        List<String> actualValue = JsonNodeUtils.getSListFieldValueByPath(objectNode, pathFields);
        assertEquals(expectedValue, actualValue);

        // try to get a values from field that does not exist
        String[] nonExistPathFields = {"last names"};
        List<String> nonExistentValue = JsonNodeUtils.getSListFieldValueByPath(objectNode, nonExistPathFields);
        assertTrue(nonExistentValue.isEmpty());
    }

    @Test
    public void testAddAFieldByPath() throws Exception {
        String json = "{\"person\": {\"name\": \"Aviv\"}}";
        String[] pathFields = {"person"};
        String key = "age";
        String value = "29";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        JsonNodeUtils.addAFieldByPath(jsonNode, pathFields, key, value);

        ObjectNode expected = objectMapper.createObjectNode();
        expected.putObject("person").put("name", "Aviv").put("age", "29");
        assertEquals(expected, jsonNode);
    }

    @Test
    public void testRemoveFieldOfListByPathAndName() throws JsonProcessingException {
        String jsonStr = "[{\"name\":\"Alice\",\"age\":30},{\"name\":\"Bob\",\"age\":25}]";
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String[] pathFields = new String[]{};
        String fieldName = "age";

        JsonNodeUtils.removeFieldOfListByPathAndName(jsonNode, pathFields, fieldName);

        // verify that the field has been removed from all list elements
        for (JsonNode node : jsonNode) {
            assertFalse(node.has(fieldName));
        }
    }


    @Test
    public void testRemoveFieldByPathAndName() throws JsonProcessingException {
        String jsonString = "{\"name\":\"Aviv\", \"age\":29}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        String[] pathFields = {};
        String fieldName = "age";
        JsonNodeUtils.removeFieldByPathAndName(jsonNode, pathFields, fieldName);
        String expectedString = "{\"name\":\"Aviv\"}";
        JsonNode expected = objectMapper.readTree(expectedString);
        assertEquals(expected, jsonNode);
    }

    @Test
    public void testRemoveAFieldByPathFieldsAndNameFieldTwoIsLIstAndFieldToBeRemovedIsFarOneLayer() throws Exception {
    }

    @Test
    public void testRemoveAFieldByPathFieldsAndNameFieldTwoIsLIst() throws JsonProcessingException {
        String jsonStr = "{ \"foo\": { \"bar\": [ { \"name\": \"Alice\", \"age\": 25 }, { \"name\": \"Bob\", \"age\": 30 } ] } }";
        JsonNode jsonNode = objectMapper.readTree(jsonStr);

        // Call the method to remove the "age" field from each object in the array
        String[] pathFields = {"foo", "bar"};
        String fieldName = "age";
        JsonNodeUtils.removeAFieldByPathFieldsAndNameFieldTwoIsLIst(jsonNode, pathFields, fieldName);

        // Assert that the "age" field has been removed from each object in the array
        JsonNode nestedNode = jsonNode.get(pathFields[0]).get(pathFields[1]);
        for (JsonNode node : nestedNode) {
            assertNull(node.get(fieldName));
        }
    }

    @Test
    public void testConvertFieldByPath() throws JsonProcessingException {

    }


    @Test
    public void testGetJsonNodeByJsonNodeAndPath() throws JsonProcessingException {
        String json = "{\"a\":{\"b\":{\"c\":\"value\"}}}";
        JsonNode jsonNode = objectMapper.readTree(json);
        String[] pathFieldsPolicyRulesDst = {"a", "b", "c"};

        JsonNode result = JsonNodeUtils.getJsonNodeByJsonNodeAndPath(jsonNode, pathFieldsPolicyRulesDst);
        assertEquals("value", result.asText());
    }

    @Test
    public void testAddPositionByPath() throws Exception {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        ObjectNode objectNode1 = JsonNodeFactory.instance.objectNode();
        objectNode1.put("id", "1");
        ObjectNode objectNode2 = JsonNodeFactory.instance.objectNode();
        objectNode2.put("id", "2");
        arrayNode.add(objectNode1);
        arrayNode.add(objectNode2);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
        rootNode.set("array", arrayNode);
        String[] pathFields = {"array"};
        String fieldName = "position";

        JsonNodeUtils.addPositionByPath(rootNode, pathFields, fieldName);

        assertEquals(0, rootNode.get("array").get(0).get("position").asInt());
        assertEquals(1, rootNode.get("array").get(1).get("position").asInt());
    }

    @Test
    public void testAddFieldToArrayFieldByPathAndFieldName() {
        String jsonString = "{\"items\": [{\"name\": \"item1\", \"category\": \"cat1\"}, {\"name\": \"item2\", \"category\": \"cat2\"}]}";
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String[] pathFields = {"items"};
        String fieldName = "readOnly";
        String objectName = "category";
        Map<String, Boolean> rulesToReadOnlyValues = new HashMap<>();
        rulesToReadOnlyValues.put("cat1", true);
        rulesToReadOnlyValues.put("cat2", false);

        JsonNode expectedOutput = jsonNode.deepCopy();
        ArrayNode itemsNode = (ArrayNode) expectedOutput.get("items");
        for (JsonNode node : itemsNode) {
            ((ObjectNode) node).put(fieldName, rulesToReadOnlyValues.get(node.get(objectName).asText()));
        }
        JsonNodeUtils.addFieldToArrayFieldByPathAndFieldName(jsonNode, pathFields, fieldName, objectName, rulesToReadOnlyValues);

        assertEquals(expectedOutput, jsonNode);
    }

    @Test
    public void testAddFieldToArrayFieldByPathAndFieldNameBooleanValue() {
        String jsonString = "{\"items\": [{\"name\": \"item1\", \"value\": 10}, {\"name\": \"item2\", \"value\": 20}]}";
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String[] pathFields = {"items"};
        String fieldName = "readOnly";
        boolean fieldValue = true;

        JsonNode expectedOutput = jsonNode.deepCopy();
        ArrayNode itemsNode = (ArrayNode) expectedOutput.get("items");
        for (JsonNode node : itemsNode) {
            ((ObjectNode) node).put(fieldName, fieldValue);
        }
        JsonNodeUtils.addFieldToArrayFieldByPathAndFieldName(jsonNode, pathFields, fieldName, fieldValue);

        // Assert that the function produced the expected output
        assertEquals(expectedOutput, jsonNode);
    }

    @Test
    public void testAddFieldToArrayFieldByPathAndFieldNameStringValue() {
        String jsonString = "{\"items\": [{\"name\": \"item1\", \"value\": 10}, {\"name\": \"item2\", \"value\": 20}]}";
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Define the input parameters for the function
        String[] pathFields = {"items"};
        String fieldName = "status";
        String fieldValue = "OK";

        // Call the function being tested
        JsonNode expectedOutput = jsonNode.deepCopy();
        ArrayNode itemsNode = (ArrayNode) expectedOutput.get("items");
        for (JsonNode node : itemsNode) {
            ((ObjectNode) node).put(fieldName, fieldValue);
        }
        JsonNodeUtils.addFieldToArrayFieldByPathAndFieldName(jsonNode, pathFields, fieldName, fieldValue);

        // Assert that the function produced the expected output
        assertEquals(expectedOutput, jsonNode);

    }

    @Test
    public void testConvertEmptyStringFieldToEmptyMap() throws JsonProcessingException {
//        String jsonStr = "{\"results\":[{\"id\":1,\"name\":\"Alice\",\"data\":[\"\",\"foo\",\"bar\"]},{\"id\":2,\"name\":\"Bob\",\"data\":[\"baz\",\"\",\"qux\"]}]}";
//        JsonNode jsonNode = objectMapper.readTree(jsonStr);
//
//        String[] pathFields = {"results", "data"};
//        String fieldName1 = "";
//        String fieldName2 = "";
//        JsonNodeUtils.convertEmptyStringFieldToEmptyMap(pathFields, jsonNode, fieldName1, fieldName2);
//
//        assertTrue(jsonNode.path("results").get(0).path("data").get(0).isObject());
//        assertTrue(jsonNode.path("results").get(1).path("data").get(1).isObject());
//        assertFalse(jsonNode.path("results").get(0).path("data").get(1).isObject());
//        assertFalse(jsonNode.path("results").get(1).path("data").get(0).isObject());
    }


    @Test
    public void testCleanFieldOfTwoEmptyFields() throws Exception {
        String jsonStr = "{\"books\": [{\"id\": 1, \"name\": \"book1\", \"details\": {\"author\": \"author1\", \"publisher\": \"publisher1\"}}, {\"id\": 2, \"name\": \"book2\", \"details\": {\"author\": \"\", \"publisher\": \"\"}}]}";
        JsonNode jsonNode = objectMapper.readTree(jsonStr);

        String[] pathFields = {"books"};
        String fieldOfTwo = "details";
        String nestedFirst = "author";
        String nestedSecond = "publisher";
        JsonNodeUtils.cleanFieldOfTwoEmptyFields(jsonNode, pathFields, fieldOfTwo, nestedFirst, nestedSecond);

        // Verify the result
        JsonNode booksNode = jsonNode.get("books");
        assertEquals(2, booksNode.size());
        assertFalse(booksNode.get(0).has("details"));
    }

    @Test
    public void testCleanArrayFieldIfArrayIsEmpty() throws Exception {
        String inputJson = "{ \"users\": [{ \"id\": 1, \"name\": \"Alice\", \"hobbies\": [\"reading\", \"swimming\"] }, { \"id\": 2, \"name\": \"Bob\", \"hobbies\": [] }]}";
        JsonNode jsonNode = objectMapper.readTree(inputJson);

        String expectedJson = "{ \"users\": [{ \"id\": 1, \"name\": \"Alice\", \"hobbies\": [\"reading\", \"swimming\"] }, { \"id\": 2, \"name\": \"Bob\" }]}";
        JsonNode expectedNode = objectMapper.readTree(expectedJson);

        String[] pathFields = new String[]{"users"};
        String[] pathFields2 = new String[]{};
        String fieldName = "hobbies";
        JsonNodeUtils.cleanArrayFieldIfArrayIsEmpty(jsonNode, pathFields, pathFields2, fieldName);

        assertTrue(expectedNode.equals(jsonNode));
    }
}