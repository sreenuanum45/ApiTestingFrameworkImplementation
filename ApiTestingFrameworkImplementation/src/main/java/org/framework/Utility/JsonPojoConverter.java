package org.framework.Utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class JsonPojoConverter {
    private static final Gson gson = new Gson();

    // Convert JSON String to a Single POJO Object
    public static <T> T jsonToPojo(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    // Convert JSON Array String to a List of POJOs
    public static <T> List<T> jsonToPojoList(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, type);
    }

    // Convert POJO Object to JSON String
    public static String pojoToJson(Object obj) {
        return gson.toJson(obj);
    }

    // Convert List of POJOs to JSON Array String
    public static String pojoListToJson(List<?> list) {
        return gson.toJson(list);
    }
}
