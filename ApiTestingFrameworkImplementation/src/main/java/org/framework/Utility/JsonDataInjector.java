package org.framework.Utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class for working with JSON data.
 * <p>
 * This class provides methods to load JSON from a file, inject/update data in a JSON object,
 * remove fields, merge JSON nodes, convert JSON nodes to pretty-printed strings, and save JSON data to a file.
 * It is designed for production use with robust error handling, logging, and dynamic functionality.
 */
public final class JsonDataInjector {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataInjector.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Static initializer to configure the ObjectMapper as needed.
    static {
        // Register Java Time module to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        // Additional configuration can be added here if needed.
        // e.g., objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // Private constructor to prevent instantiation.
    private JsonDataInjector() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Loads JSON content from a file.
     *
     * @param filePath the path to the JSON file
     * @return the parsed JsonNode
     * @throws RuntimeException if file reading or parsing fails
     */
    public static JsonNode loadJsonFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                String message = "File not found: " + filePath;
                logger.error(message);
                throw new IOException(message);
            }
            logger.info("Loading JSON from file: {}", filePath);
            String jsonContent = Files.readString(path);
            return objectMapper.readTree(jsonContent);
        } catch (IOException e) {
            logger.error("Error loading JSON from file: {}", filePath, e);
            throw new RuntimeException("Error loading JSON from file: " + filePath, e);
        }
    }

    /**
     * Saves JSON content to a file.
     *
     * @param jsonNode   the JSON content to save
     * @param outputPath the path where the JSON file should be saved
     * @throws RuntimeException if writing to the file fails
     */
    public static void saveJsonToFile(JsonNode jsonNode, String outputPath) {
        try {
            logger.info("Saving JSON to file: {}", outputPath);
            String jsonString = jsonToString(jsonNode);
            Files.writeString(Paths.get(outputPath), jsonString);
        } catch (IOException e) {
            logger.error("Error writing JSON to file: {}", outputPath, e);
            throw new RuntimeException("Error writing JSON to file: " + outputPath, e);
        }
    }

    /**
     * Converts a JsonNode to a formatted JSON string.
     *
     * @param jsonNode the JSON node to convert
     * @return a pretty-printed JSON string
     * @throws RuntimeException if serialization fails
     */
    public static String jsonToString(JsonNode jsonNode) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (IOException e) {
            logger.error("Error serializing JSON to string", e);
            throw new RuntimeException("Error serializing JSON to string", e);
        }
    }

    /**
     * Injects or updates data in a JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param dataToInject the map of key-value pairs to inject/update
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode injectData(JsonNode originalJson, Map<String, Object> dataToInject) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ObjectNode modifiedJson = (ObjectNode) originalJson;
        dataToInject.forEach((key, value) -> {
            logger.info("Injecting/updating key: {} with value: {}", key, value);
            modifiedJson.set(key, objectMapper.valueToTree(value));
        });
        return modifiedJson;
    }

    /**
     * Adds a JSON array node to an existing JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name for the JSON array
     * @param arrayNode    the JSON array node to add
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode addJsonArray(JsonNode originalJson, String key, JsonNode arrayNode) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ((ObjectNode) originalJson).set(key, arrayNode);
        return originalJson;
    }

    /**
     * Adds a JSON object node to an existing JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name for the JSON object
     * @param objectNode   the JSON object node to add
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode addJsonObject(JsonNode originalJson, String key, JsonNode objectNode) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ((ObjectNode) originalJson).set(key, objectNode);
        return originalJson;
    }

    /**
     * Adds a JSON string value to an existing JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name
     * @param value        the string value to add
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode addJsonString(JsonNode originalJson, String key, String value) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ((ObjectNode) originalJson).put(key, value);
        return originalJson;
    }

    /**
     * Adds a JSON number value to an existing JSON object.
     * Dynamically determines the appropriate numeric type.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name
     * @param value        the number value to add
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode addJsonNumber(JsonNode originalJson, String key, Number value) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ObjectNode node = (ObjectNode) originalJson;
        if (value instanceof Integer) {
            node.put(key, value.intValue());
        } else if (value instanceof Long) {
            node.put(key, value.longValue());
        } else if (value instanceof Double) {
            node.put(key, value.doubleValue());
        } else if (value instanceof Float) {
            node.put(key, value.floatValue());
        } else if (value instanceof Short) {
            node.put(key, value.shortValue());
        } else if (value instanceof Byte) {
            node.put(key, value.byteValue());
        } else if (value instanceof BigDecimal) {
            node.put(key, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            node.put(key, (BigInteger) value);
        } else {
            // Fallback: convert to string representation
            node.put(key, value.toString());
        }
        return node;
    }

    /**
     * Adds a JSON boolean value to an existing JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name
     * @param value        the boolean value to add
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode addJsonBoolean(JsonNode originalJson, String key, Boolean value) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ((ObjectNode) originalJson).put(key, value);
        return originalJson;
    }

    /**
     * Adds a JSON null value to an existing JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode addJsonNull(JsonNode originalJson, String key) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ((ObjectNode) originalJson).putNull(key);
        return originalJson;
    }

    /**
     * Adds a generic JSON payload (JsonNode) to an existing JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name
     * @param payload      the JSON payload to add
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode addJsonPayload(JsonNode originalJson, String key, JsonNode payload) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ((ObjectNode) originalJson).set(key, payload);
        return originalJson;
    }

    /**
     * Adds additional payload to a JSON response.
     * <p>
     * If the provided response is an ObjectNode, the payload is injected directly.
     * Otherwise, the response is wrapped in a new ObjectNode under the "response" key before
     * the additional payload is added.
     * </p>
     *
     * @param response          the original JSON response
     * @param additionalPayload the map of key-value pairs representing the additional payload
     * @return a JSON node containing the original response (or its wrapper) along with the additional payload
     */
    public static JsonNode addAdditionalPayload(JsonNode response, Map<String, Object> additionalPayload) {
        ObjectNode result;
        if (response.isObject()) {
            result = (ObjectNode) response;
        } else {
            result = objectMapper.createObjectNode();
            result.set("response", response);
        }
        additionalPayload.forEach((key, value) -> {
            logger.info("Adding additional payload: {} = {}", key, value);
            result.set(key, objectMapper.valueToTree(value));
        });
        return result;
    }

    /**
     * Removes a field from a JSON object.
     *
     * @param originalJson the original JSON node (must be an ObjectNode)
     * @param key          the field name to remove
     * @return the modified JsonNode
     * @throws IllegalArgumentException if the provided JSON is not an object
     */
    public static JsonNode removeJsonField(JsonNode originalJson, String key) {
        if (!originalJson.isObject()) {
            String message = "JSON root must be an object. Provided JSON type: " + originalJson.getNodeType();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ((ObjectNode) originalJson).remove(key);
        return originalJson;
    }

    /**
     * Recursively merges two JSON objects.
     * Fields in the source ObjectNode will overwrite or be merged with fields in the target.
     *
     * @param target the target ObjectNode to merge into
     * @param source the source ObjectNode to merge from
     * @return the merged ObjectNode
     */
    public static ObjectNode mergeJsonNodes(ObjectNode target, ObjectNode source) {
        Iterator<Map.Entry<String, JsonNode>> fields = source.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey();
            JsonNode sourceValue = entry.getValue();
            if (target.has(fieldName)) {
                JsonNode targetValue = target.get(fieldName);
                if (targetValue.isObject() && sourceValue.isObject()) {
                    mergeJsonNodes((ObjectNode) targetValue, (ObjectNode) sourceValue);
                } else {
                    target.set(fieldName, sourceValue);
                }
            } else {
                target.set(fieldName, sourceValue);
            }
        }
        return target;
    }

    /**
     * Converts an arbitrary object to a JsonNode.
     *
     * @param obj the object to convert
     * @return the resulting JsonNode
     */
    public static JsonNode convertObjectToJsonNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }
}
