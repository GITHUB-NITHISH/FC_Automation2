package com.automation.dataprovider;

import com.automation.utils.LoggerUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonDataReader - Reads JSON test data files
 */
public class JsonDataReader {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Read JSON data from file
     */
    public static JsonNode readJsonData(String filePath) {
        try {
            logger.info("Reading JSON data from: " + filePath);
            return objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            logger.error("Failed to read JSON file: " + filePath + " Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get string value from JSON
     */
    public static String getStringValue(JsonNode node, String key) {
        try {
            if (node != null && node.has(key)) {
                return node.get(key).asText();
            }
        } catch (Exception e) {
            logger.error("Failed to get string value for key: " + key + " Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get integer value from JSON
     */
    public static int getIntValue(JsonNode node, String key) {
        try {
            if (node != null && node.has(key)) {
                return node.get(key).asInt();
            }
        } catch (Exception e) {
            logger.error("Failed to get int value for key: " + key + " Error: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get boolean value from JSON
     */
    public static boolean getBooleanValue(JsonNode node, String key) {
        try {
            if (node != null && node.has(key)) {
                return node.get(key).asBoolean();
            }
        } catch (Exception e) {
            logger.error("Failed to get boolean value for key: " + key + " Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Convert JSON node to map
     */
    public static Map<String, Object> jsonToMap(JsonNode node) {
        Map<String, Object> map = new HashMap<>();
        try {
            node.fields().forEachRemaining(entry -> map.put(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            logger.error("Failed to convert JSON to map: " + e.getMessage());
        }
        return map;
    }
    
    public static JsonNode getJsonNode(String key) {
        try {
            JsonNode root = objectMapper.readTree(
                    new File("src/test/resources/testdata/json/StaticData.json")
            );
            return root.get(key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON node: " + key, e);
        }
    }
}
