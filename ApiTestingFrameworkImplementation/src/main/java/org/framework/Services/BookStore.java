package org.framework.Services;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookStore {
    private String baseUri;
    private RequestSpecification request;

    public BookStore(final String baseUri, final String servicePath) throws Exception {
        this.baseUri = baseUri;
        RestAssured.baseURI = baseUri;
        this.request = RestAssured.given();
    }

    public Response GetAllBookDetails(String endpoint) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        Response response = request.get(fullUrl);
        System.out.println("Status code: " + response.getStatusCode() + "   " + "Response Message: " + response.getBody().prettyPrint());
        return response;
    }

    public Response addListOfBooks(String endpoint, String payload, String token) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.header("Authorization", "Bearer " + token);
        request.body(payload);
        Response response = request.post(fullUrl);
        System.out.println("Status code: " + response.getStatusCode() + "   " + "Response Message: " + response.getBody().prettyPrint());
        return response;
    }

    public Response getBook(String endpoint, String token, String isbn) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.header("Authorization", "Bearer " + token);
        request.queryParam("ISBN", isbn);
        Response response = request.get(fullUrl);
        System.out.println("Status code: " + response.getStatusCode() + "   " + "Response Message: " + response.getBody().prettyPrint());
        return response;
    }

    public Response updateISBN(String endpoint, String payload, String token, String isbn) throws Exception {
        String fullUrl = baseUri + endpoint + "/" + isbn;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.header("Authorization", "Bearer " + token);
        request.body(payload);
        Response response = request.put(fullUrl);
        System.out.println("Status code: " + response.getStatusCode() + "   " + "Response Message: " + response.getBody().prettyPrint());
        return response;
    }

    public Response deleteBook(String endpoint, String token, String body) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.header("Authorization", "Bearer " + token);
        request.body(body);
        Response response = request.delete(fullUrl);
        System.out.println("Status code: " + response.getStatusCode() + "   " + "Response Message: " + response.getBody().prettyPrint());
        return response;
    }

    public Response DeleteBooks(String endpoint, String token, String uid) throws Exception {
        String fullUrl = baseUri + endpoint;
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.queryParam("uid", uid);
        request.header("Authorization", "Bearer " + token);
        Response response = request.delete(fullUrl);
        System.out.println("Status code: " + response.getStatusCode() + "   " + "Response Message: " + response.getBody().prettyPrint());
        return response;
    }
}



