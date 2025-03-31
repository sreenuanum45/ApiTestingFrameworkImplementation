// src/main/java/endpoints/UserEndpoints.java
package org.framework.endpoints;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserEndpoints {

    // GET /users - Retrieve all users
    public static Response getAllUsers() {
        return given()
                .when()
                .get("/users")
                .then()
                .extract()
                .response();
    }

    // POST /users - Create a new user
    public static Response createUser(String body) {
        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();
    }

    // GET /users/{id} - Retrieve a single user by ID
    public static Response getUserById(int id) {
        return given()
                .when()
                .get("/users/" + id)
                .then()
                .extract()
                .response();
    }

    // PUT /users/{id} - Update an existing user by ID
    public static Response updateUser(int id, String body) {
        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .put("/users/" + id)
                .then()
                .extract()
                .response();
    }

    // DELETE /users/{id} - Delete a user by ID
    public static Response deleteUser(int id) {
        return given()
                .when()
                .delete("/users/" + id)
                .then()
                .extract()
                .response();
    }
}
