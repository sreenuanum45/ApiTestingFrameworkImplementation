package org.framework.Utility;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApiUtil {
    private static final Logger logger = LoggerFactory.getLogger(ApiUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Properties config = new Properties();
    private static final Map<String, String> defaultHeaders = new HashMap<>();
    private static int retryCount = 3;
    private static long retryDelay = 1000; // milliseconds

    static {
        loadConfig();
        configureRestAssured();
    }

    private static void loadConfig() {
        try (InputStream input = ApiUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                config.load(input);
            }
        } catch (IOException e) {
            logger.warn("Unable to load config.properties file");
        }

        // Set default base URL from config or environment variable
        String baseUrl = config.getProperty("base.url", System.getenv("API_BASE_URL"));
        if (baseUrl != null) {
            RestAssured.baseURI = baseUrl;
        }
    }

    private static void configureRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        if (Boolean.parseBoolean(config.getProperty("ssl.verification.disable", "false"))) {
            RestAssured.useRelaxedHTTPSValidation();
        }
    }

    // region Configuration Methods
    public static void setBaseUrl(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    public static void setAuthentication(AuthenticationScheme authentication) {
        RestAssured.authentication = authentication;
    }

    public static void addDefaultHeader(String name, String value) {
        defaultHeaders.put(name, value);
    }

    public static void setRetryPolicy(int count, long delayMillis) {
        retryCount = count;
        retryDelay = delayMillis;
    }
    // endregion

    // region Core HTTP Methods
    public static <T> T get(String endpoint, Class<T> responseType) {
        return get(endpoint, null, null, responseType);
    }

    public static <T> T get(String endpoint, Map<String, Object> queryParams,
                            Map<String, String> headers, Class<T> responseType) {
        return executeWithRetry(() -> {
            Response response = createRequest(queryParams, headers)
                    .get(endpoint);
            return processResponse(response, responseType);
        });
    }

    public static <T> Response post(String endpoint, Object body) {
        return post(endpoint, body, null, null);
    }

    public static <T> Response post(String endpoint, Object body,
                                    Map<String, String> headers, Map<String, Object> queryParams) {
        return executeWithRetry(() -> {
            Response response = createRequest(queryParams, headers)
                    .body(convertToJson(body))
                    .post(endpoint);
            return processResponse(response, Response.class);
        });
    }

    public static <T> T put(String endpoint, Object body, Class<T> responseType) {
        return put(endpoint, body, null, null, responseType);
    }

    public static <T> T put(String endpoint, Object body, Map<String, String> headers,
                            Map<String, Object> queryParams, Class<T> responseType) {
        return executeWithRetry(() -> {
            Response response = createRequest(queryParams, headers)
                    .body(convertToJson(body))
                    .put(endpoint);
            return processResponse(response, responseType);
        });
    }

    public static Response delete(String endpoint) {
        return delete(endpoint, null, null);
    }

    public static Response delete(String endpoint, Map<String, String> headers,
                                  Map<String, Object> queryParams) {
        return executeWithRetry(() -> {
            Response response = createRequest(queryParams, headers)
                    .delete(endpoint);
            return processResponse(response, Response.class);
        });
    }
    // endregion

    // region File Upload
    public static Response uploadFile(String endpoint, File file, String mimeType) {
        return uploadFile(endpoint, file, mimeType, null, null);
    }

    public static Response uploadFile(String endpoint, File file, String mimeType,
                                      Map<String, String> headers, Map<String, Object> queryParams) {
        return executeWithRetry(() -> {
            Response response = createRequest(queryParams, headers)
                    .multiPart("file", file, mimeType)
                    .post(endpoint);
            return processResponse(response, Response.class);
        });
    }
    // endregion

    // region Utility Methods
    private static RequestSpecification createRequest(Map<String, Object> queryParams,
                                                      Map<String, String> headers) {
        RequestSpecification request = RestAssured.given();

        // Add default headers
        defaultHeaders.forEach(request::header);

        // Add method-specific headers
        if (headers != null) {
            headers.forEach(request::header);
        }

        // Add query parameters
        if (queryParams != null) {
            queryParams.forEach(request::queryParam);
        }

        return request;
    }

    private static <T> T processResponse(Response response, Class<T> responseType) {
        logResponseDetails(response);
        validateResponse(response);

        if (responseType == Response.class) {
            return responseType.cast(response);
        }
        return convertJsonToPojo(response, responseType);
    }

    private static void validateResponse(Response response) {
        int statusCode = response.getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            String errorBody = response.getBody().asString();
            logger.error("API request failed - Status: {} - Body: {}", statusCode, errorBody);
            throw new ApiException(statusCode, "API request failed: " + errorBody);
        }
    }

    private static <T> T convertJsonToPojo(Response response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response.getBody().asString(), clazz);
        } catch (IOException e) {
            logger.error("JSON deserialization error: {}", e.getMessage());
            throw new SerializationException("JSON deserialization error", e);
        }
    }

    private static String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("JSON serialization error: {}", e.getMessage());
            throw new SerializationException("JSON serialization error", e);
        }
    }

    private static void logResponseDetails(Response response) {
        logger.info("Response Status: {}", response.getStatusCode());
        logger.debug("Response Headers: {}", response.getHeaders());
        logger.debug("Response Body: {}", response.getBody().asPrettyString());
    }

    private static <T> T executeWithRetry(ApiOperation<T> operation) {
        int attempts = 0;
        Exception lastError = null;

        while (attempts < retryCount) {
            try {
                return operation.execute();
            } catch (ApiException e) {
                if (e.getStatusCode() >= 500) { // Retry only on server errors
                    lastError = e;
                    logger.warn("Attempt {} failed - Retrying in {}ms", attempts + 1, retryDelay);
                    sleep(retryDelay);
                } else {
                    throw e;
                }
            }
            attempts++;
        }
        throw new ApiException("API request failed after " + retryCount + " attempts", lastError);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    // endregion

    // region Custom Exceptions
    public static class ApiException extends RuntimeException {
        private final int statusCode;

        public ApiException(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }

        public ApiException(String message, Throwable cause) {
            super(message, cause);
            this.statusCode = -1;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    public static class SerializationException extends RuntimeException {
        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class DeserializationException extends RuntimeException {
        public DeserializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }



    @FunctionalInterface
    private interface ApiOperation<T> {
        T execute();
    }
    // endregion
}