package org.framework.Services.PetstoreService;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Store {
    public Response CreateOrder(String body, String endpoint) {
           return given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(endpoint);
    }
    public Response findThePurchaseOrderById(String endpoint, int orderId) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("orderId", orderId)
                        .get(endpoint);
    }
    public Response ReturnsPetInventoriesByStatus(String endpoint) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .get(endpoint);
    }
    public Response DeleteOrder(String endpoint, int orderId) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("orderId", orderId)
                        .delete(endpoint);
    }
}
