package org.framework.Utility;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

public class RestUtils {

    public static Response executeApiRequest(Map<String, String> testData) {
        String method = testData.get("Request Method").toUpperCase();
        String endpoint = testData.get("API Endpoint");
        String headers = testData.get("Request Headers");
        String payload = testData.get("Request Payload / Params");

        io.restassured.specification.RequestSpecification request = given();

        // Add headers dynamically
        if (!headers.isEmpty()) {
            String[] headerPairs = headers.split(";");
            for (String pair : headerPairs) {
                String[] keyValue = pair.split(":");
                request.header(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        // Set request payload
        if (!payload.isEmpty()) {
            request.body(payload);
        }

        Response response = null;
        switch (method) {
            case "GET":
                response = request.when().get(endpoint);
                break;
            case "POST":
                response = request.when().post(endpoint);
                break;
            case "PUT":
                response = request.when().put(endpoint);
                break;
            case "DELETE":
                response = request.when().delete(endpoint);
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
        return response;
    }
    private static Map<String, String> parseHeaders(String headers) {
        Map<String, String> headerMap = new HashMap<>();
        if (!headers.isEmpty()) {
            String[] headerPairs = headers.split(";");
            for (String pair : headerPairs) {
                String[] keyValue = pair.split(":");
                headerMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return headerMap;
    }
}
