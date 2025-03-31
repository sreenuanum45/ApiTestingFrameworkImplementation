package org.framework.Services.PetstoreService;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class Pet {
    public Response AddNewPetInStore(String payload, String endpoint) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .post(endpoint);
    }
    public Response putPet(String payload, String endpoint) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .put(endpoint);
    }
    public  Response pet_FindByStatus(String endpoint, String status) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .queryParam("status", status)
                        .get(endpoint);
    }
    public Response FindPetByID(String endpoint, int petId) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("petId", petId)
                        .get(endpoint);
    }
    public Response Updates_A_PetInTheStoreWithForm(String endpoint, int petId, String name, String status)
    {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("petId", petId)
                        .formParam("name", name)
                        .formParam("status", status)
                        .get(endpoint);
    }
    public Response UploadImageToPet(String endpoint, int petId, String path) {
        File file = new File(path);

        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + file.getAbsolutePath());
        }
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.MULTIPART)
                .pathParam("petId", petId)
                .multiPart("file", file)  // Use File object instead of path string
                .post(endpoint);
    }
    public Response DeletePetID(String endpoint, int petId) {
        return
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .pathParam("petId", petId)
                        .delete(endpoint);
    }

}
