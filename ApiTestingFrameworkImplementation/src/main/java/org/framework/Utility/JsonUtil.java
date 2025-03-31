package org.framework.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility class for converting between JSON strings, Maps, and objects.
 * <p>
 * This class provides methods using both Jackson and Gson:
 * <ul>
 *   <li>Jackson-based methods for converting JSON strings to maps/objects and vice versa.</li>
 *   <li>Gson-based methods for working with Gson’s JsonObject/JsonArray and recursive conversions.</li>
 * </ul>
 */
public class JsonUtil {
    // Jackson instance for JSON string/object conversion
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // Gson instance for JSON string and JsonObject conversions
    private static final Gson gson = new Gson();

    // ============================================================
    // Jackson-based Methods
    // ============================================================

    /**
     * Convert a JSON string to a Map<String, Object> using Jackson.
     *
     * @param json the JSON string
     * @return a Map representation of the JSON
     */
    public static Map<String, Object> jsonToMap(String json) {
        try {
            // Using HashMap.class to indicate the target type.
            return objectMapper.readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to HashMap", e);
        }
    }

    /**
     * Convert a Map<String, Object> to a JSON string using Jackson.
     *
     * @param map the Map to convert
     * @return a JSON string representation of the map
     */
    public static String mapToJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting HashMap to JSON", e);
        }
    }

    /**
     * Convert a JSON string to an object of type T using Jackson.
     *
     * @param json the JSON string
     * @param cls  the target class
     * @param <T>  the type of the target object
     * @return an instance of T populated with data from the JSON string
     * @throws IOException if conversion fails
     */
    public static <T> T convertJsonToObject(String json, Class<T> cls) throws IOException {
        return objectMapper.readValue(json, cls);
    }

    /**
     * Convert an object to a JSON string using Jackson.
     *
     * @param obj the object to convert
     * @return a JSON string representation of the object
     * @throws IOException if conversion fails
     */
    public static String convertObjectToJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    // ============================================================
    // Gson-based Methods
    // ============================================================

    /**
     * Convert a JSON string to a Map<String, Object> using Gson.
     * This is an alternate implementation that uses Gson.
     *
     * @param json the JSON string
     * @return a Map representation of the JSON
     */
    public static Map<String, Object> jsonToMapGson(String json) {
        Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Convert a Gson JsonObject to a Map<String, Object>.
     *
     * @param json the JsonObject
     * @return a Map representation of the JsonObject
     */
    public static Map<String, Object> jsonToMap(JsonObject json) {
        Map<String, Object> returnMap = new HashMap<>();
        if (json != null && !json.isJsonNull()) {
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                returnMap.put(entry.getKey(), entry.getValue());
            }
        }
        return returnMap;
    }

    /**
     * Convert a Map<String, Object> to a JSON string using Gson.
     * This is an alternate implementation that uses Gson.
     *
     * @param map the Map to convert
     * @return a JSON string representation of the map
     */
    public static String mapToJsonGson(Map<String, Object> map) {
        return gson.toJson(map);
    }

    /**
     * Recursively convert a Gson JsonObject to a Map<String, Object>.
     * This method handles nested JsonObjects and JsonArrays.
     *
     * @param json the JsonObject to convert
     * @return a Map representation of the JsonObject
     */
    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<>();
        for (String key : json.keySet()) {
            JsonElement value = json.get(key);
            if (value.isJsonArray()) {
                map.put(key, toList(value.getAsJsonArray()));
            } else if (value.isJsonObject()) {
                map.put(key, toMap(value.getAsJsonObject()));
            } else if (value.isJsonNull()) {
                map.put(key, null);
            } else {
                map.put(key, value.getAsString());
            }
        }
        return map;
    }

    /**
     * Recursively convert a Gson JsonArray to a List<Object>.
     * This method handles nested JsonArrays and JsonObjects.
     *
     * @param jsonArray the JsonArray to convert
     * @return a List representation of the JsonArray
     */
    public static List<Object> toList(JsonArray jsonArray) {
        List<Object> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            if (element.isJsonArray()) {
                list.add(toList(element.getAsJsonArray()));
            } else if (element.isJsonObject()) {
                list.add(toMap(element.getAsJsonObject()));
            } else if (element.isJsonNull()) {
                list.add(null);
            } else {
                list.add(element.getAsString());
            }
        }
        return list;
    }
}
