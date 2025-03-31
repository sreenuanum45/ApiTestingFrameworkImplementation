package org.framework.Services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Account {
    private String baseUri;
    private RequestSpecification request;
    public Account(final String baseUri, final String servicePath) throws Exception {
        this.baseUri = baseUri;
        RestAssured.baseURI = baseUri;
        this.request = RestAssured.given();
    }
    public Response CreateAccount(String endpoint, String payload) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.body(payload);
        Response response = request.post(fullUrl);
        System.out.println("Status code: "+response.getStatusCode()+"   "+"Response Message: "+response.getBody().prettyPrint());
        return response;
    }
    public Response GenerateToken(String endpoint, String payload) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.body(payload);
        Response response = request.post(fullUrl);
        System.out.println("Status code: "+response.getStatusCode()+"   "+"Response Message: "+response.getBody().prettyPrint());
        return response;
    }
    public Response AuthenticateUser(String endpoint, String payload) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.body(payload);
        Response response = request.post(fullUrl);
        System.out.println("Status code: "+response.getStatusCode()+"   "+"Response Message: "+response.getBody().prettyPrint());
        return response;
    }
    public Response GetAccountDetails(String endpoint) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        Response response = request.get(fullUrl);
        System.out.println("Status code: "+response.getStatusCode()+"   "+"Response Message: "+response.getBody().prettyPrint());
        return response;
    }
    public Response deleteUser(String endpoint, String token) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.header("Authorization", "Bearer "+token);
        Response response = request.delete(fullUrl);
        System.out.println("Status code: "+response.getStatusCode()+"   "+"Response Message: "+response.getBody().prettyPrint());
        return response;
    }
}
