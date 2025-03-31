package org.framework.Services.PetstoreService;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class User {
    public Response CreateUser(String body, String endpoint) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .body(body)
                        .post(endpoint);
    }

    public Response CreateListOfUsers(String body, String endpoint) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .body(body)
                        .post(endpoint);
    }

    public Response Loginuser(String endpoint, String username, String password) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .queryParam("username", username)
                        .queryParam("password", password)
                        .get(endpoint);
    }
    public Response GetUser(String endpoint, String username) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("username", username)
                        .get(endpoint);
    }
    public Response updateUser(String endpoint, String username, String body) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("username", username)
                        .body(body)
                        .put(endpoint);
    }
    public Response deleteUser(String endpoint, String username) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("username", username)
                        .delete(endpoint);
    }
    public Response Userlogout(String endpoint) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .get(endpoint);
    }
}
