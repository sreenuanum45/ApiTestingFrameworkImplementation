// src/test/java/tests/UserAPITest.java
package tests;

import base.BaseTest;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UserAPITest extends BaseTest {

    @Test(priority = 1)
    public void testGetAllUsers() {
        Response response = org.framework.endpoints.UserEndpoints.getAllUsers();
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 for getting all users.");
    }

    @Test(priority = 2)
    public void testCreateUser() {
        String requestBody = "{ \"name\": \"John Doe\", \"email\": \"john.doe@example.com\" }";
        Response response = org.framework.endpoints.UserEndpoints.createUser(requestBody);
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201 for user creation.");
        int userId = response.jsonPath().getInt("id");
        Assert.assertTrue(userId > 0, "User id should be greater than 0.");
    }

    @Test(priority = 3)
    public void testGetUserById() {
        // First, create a user to get later.
        String requestBody = "{ \"name\": \"Jane Doe\", \"email\": \"jane.doe@example.com\" }";
        Response createResponse = org.framework.endpoints.UserEndpoints.createUser(requestBody);
        int userId = createResponse.jsonPath().getInt("id");

        // Now retrieve the created user.
        Response response = org.framework.endpoints.UserEndpoints.getUserById(userId);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 for fetching the user by id.");
        Assert.assertEquals(response.jsonPath().getString("name"), "Jane Doe", "User name should match.");
    }

    @Test(priority = 4)
    public void testUpdateUser() {
        // Create a user to update.
        String requestBody = "{ \"name\": \"Alice\", \"email\": \"alice@example.com\" }";
        Response createResponse = org.framework.endpoints.UserEndpoints.createUser(requestBody);
        int userId = createResponse.jsonPath().getInt("id");

        // Update the user.
        String updateBody = "{ \"name\": \"Alice Updated\", \"email\": \"alice.updated@example.com\" }";
        Response updateResponse = org.framework.endpoints.UserEndpoints.updateUser(userId, updateBody);
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "Expected status code 200 for updating the user.");

        // Verify the update.
        Response getResponse = org.framework.endpoints.UserEndpoints.getUserById(userId);
        Assert.assertEquals(getResponse.jsonPath().getString("name"), "Alice Updated", "User name should be updated.");
    }

    @Test(priority = 5)
    public void testDeleteUser() {
        // Create a user to delete.
        String requestBody = "{ \"name\": \"Bob\", \"email\": \"bob@example.com\" }";
        Response createResponse = org.framework.endpoints.UserEndpoints.createUser(requestBody);
        int userId = createResponse.jsonPath().getInt("id");

        // Delete the user.
        Response deleteResponse = org.framework.endpoints.UserEndpoints.deleteUser(userId);
        Assert.assertEquals(deleteResponse.getStatusCode(), 204, "Expected status code 204 for user deletion.");

        // Verify that the user is deleted.
        Response getResponse = org.framework.endpoints.UserEndpoints.getUserById(userId);
        Assert.assertEquals(getResponse.getStatusCode(), 404, "Expected status code 404 when fetching a deleted user.");
    }
}
