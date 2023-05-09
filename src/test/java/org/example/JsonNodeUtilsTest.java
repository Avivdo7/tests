package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;


import java.util.List;

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
    void testAddAIntFieldRootLevel() {

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

        // try to get false field
        String[] invalidPathFields = {"height"};
        String invalidValue = JsonNodeUtils.getStringFieldValueByPath(jsonNode, invalidPathFields);
        assertEquals(null, invalidValue);
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
    void testRemoveFieldOfListByPathAndName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "[{\"name\":\"Alice\",\"age\":30},{\"name\":\"Bob\",\"age\":25}]";
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonStr);
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
    void removeFieldByPathAndName() {

    }

    @Test
    void removeAFieldByPathFieldsAndNameFieldTwoIsLIstAndFieldToBeRemovedIsFarOneLayer() {
    }

    @Test
    void removeAFieldByPathFieldsAndNameFieldTwoIsLIst() {
    }

    @Test
    void convertFieldByPath() {
    }

    @Test
    void getJsonNodeByJsonNodeAndPath() {
    }

    @Test
    void addPositionByPath() {
    }

    @Test
    void addFieldToArrayFieldByPathAndFieldName() {
    }

    @Test
    void testAddFieldToArrayFieldByPathAndFieldName() {
    }

    @Test
    void testAddFieldToArrayFieldByPathAndFieldName1() {
    }

    @Test
    void convertEmptyStringFieldToEmptyMap() {
    }

    @Test
    void cleanFieldOfTwoEmptyFields() {
    }

    @Test
    void cleanArrayFieldIfArrayIsEmpty() {
    }
}