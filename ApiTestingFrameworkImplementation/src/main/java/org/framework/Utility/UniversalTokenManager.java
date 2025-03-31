package org.framework.Utility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class UniversalTokenManager {
    private static final Map<String, TokenData> tokenCache = new HashMap<>();
    private static final Map<String, AuthConfig> authConfigs = new HashMap<>();

    public static void configureAuth(String configName, AuthConfig config) {
        authConfigs.put(configName, config);
    }

    public static String getToken(String configName) {
        AuthConfig config = authConfigs.get(configName);
        if (config == null) throw new RuntimeException("Configuration not found: " + configName);

        TokenData tokenData = tokenCache.get(configName);
        if (tokenData != null && !isTokenExpired(tokenData)) {
            return tokenData.token;
        }

        return refreshToken(configName);
    }

    public static String refreshToken(String configName) {
        AuthConfig config = authConfigs.get(configName);
        Response response = executeAuthRequest(config);
        TokenData newToken = extractTokenData(response, config);
        tokenCache.put(configName, newToken);
        return newToken.token;
    }

    private static Response executeAuthRequest(AuthConfig config) {
        return RestAssured.given()
                .baseUri(config.baseUrl)
                .headers(config.headers)
                .contentType(config.contentType)
                .body(config.requestBody)
                .request(config.httpMethod, config.authEndpoint);
    }

    private static TokenData extractTokenData(Response response, AuthConfig config) {
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Authentication failed. Status: " + response.getStatusCode());
        }

        String token = response.jsonPath().getString(config.tokenPath);
        Long expiresIn = response.jsonPath().getLong(config.expiryPath);

        return new TokenData(
                token,
                expiresIn != null ? System.currentTimeMillis() + (expiresIn * 1000) : null
        );
    }

    private static boolean isTokenExpired(TokenData tokenData) {
        if (tokenData.expiration == null) return false; // Assume never expires
        return System.currentTimeMillis() >= tokenData.expiration;
    }

    // Configuration and Data Classes
    public static class AuthConfig {
        String baseUrl;
        String authEndpoint;
        String httpMethod = "POST";
        ContentType contentType = ContentType.JSON;
        Map<String, String> headers = new HashMap<>();
        Map<String, String> formParams = new HashMap<>();
        String requestBody;
        String tokenPath = "token";
        String expiryPath = "expires_in";

        // Builder methods would go here
    }

    private static class TokenData {
        String token;
        Long expiration;

        TokenData(String token, Long expiration) {
            this.token = token;
            this.expiration = expiration;
        }
    }
}