package tests;

import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.framework.Services.PetstoreService.Pet;
import org.framework.Services.PetstoreService.Routes;

import org.framework.Services.PetstoreService.User;
import org.framework.Utility.PayloadBuildUtil;
import org.framework.pojo.Store;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class PetStoreApi extends BaseTest {
    public String Createjson;
    public String ListOfUsersCreatejson;
    public static String username;
    public String UpdatedUsername;
    public String password;
    public String jsonpayload;
    public String id;
    public int orderId;
    public int petId;

    @BeforeClass
    public void Setup() {
        Createjson = PayloadBuildUtil.getDPSPayload1();
        ListOfUsersCreatejson = PayloadBuildUtil.getDpsPayload2();
    }

    @Test(priority = 1)
    public void CreateUser() throws JsonProcessingException {
        User user = new User();

        Response response = user.CreateUser(Createjson, Routes.postUser);
        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 for user creation.");
    }

    @Test(priority = 2)
    public void CreateUser_createWithArray() {
        User user = new User();
        Response response = user.CreateListOfUsers(ListOfUsersCreatejson, Routes.PostUser_createWithArray);
        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 for user creation.");
        Assert.assertEquals(response.jsonPath().getString("message"), "ok");
    }

    @Test(priority = 3)
    public void GetUserUsername() {
        User user = new User();
        Response response = user.GetUser(Routes.getUser, "Sreenu220");
        System.out.println(response.asPrettyString());

    }

    @Test(priority = 4, dependsOnMethods = "GetUserUsername")
    public void LoginUser() {
        User user = new User();
        Response response = user.Loginuser(Routes.loginUser, "Sreenu220", "Sreenu80@");
        password = response.getBody().jsonPath().getString("password");
        username = response.getBody().jsonPath().getString("username");
        jsonpayload = response.then().extract().body().asString();
        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 for user creation.");
    }

    @Test(priority = 5)
    public void UpdateUser() throws JsonProcessingException {
        User user = new User();
        // Parse the JSON payload
        // 1. Parse to mutable Map
        Map<String, Object> payloadMap = JsonPath.parse(Createjson).read("$");
// 2. Modify username
        UpdatedUsername="newSreenu";
        payloadMap.put("username",  UpdatedUsername);
// 3. Convert back to JSON string
        String modifiedPayload = new ObjectMapper().writeValueAsString(payloadMap);
        Response response = user.updateUser(Routes.putUser, payloadMap.get("username").toString(), modifiedPayload);
        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 for user creation.");
    }

    @Test(priority = 6)
    public void placedThePetOrderInStore() throws JsonProcessingException {
        Faker faker = new Faker();
        org.framework.pojo.Store store = new Store();
        org.framework.Services.PetstoreService.Store store1 = new org.framework.Services.PetstoreService.Store();
        store.setOrderId(faker.number().numberBetween(1, 10));
        store.setPetId(faker.number().numberBetween(100, 1000));
        store.setQuantity(faker.number().randomDigit());
        store.setShipDate("2025-03-29T10:20:41.888Z");
        store.setStatus("placed");
        store.setComplete(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(store);
        Store S = objectMapper.readValue(payload, Store.class);
        orderId = S.getOrderId();
        System.out.println(payload);
        Response response = store1.CreateOrder(payload, Routes.postOrder);
        response.then().log().all();
        System.out.println(response.asPrettyString());
        id = response.getBody().jsonPath().getString("id");
        petId = response.getBody().jsonPath().getInt("petId");
    }

    @Test(priority = 7, dependsOnMethods = "placedThePetOrderInStore")
    public void findThePurchaseOrderById() {
        org.framework.Services.PetstoreService.Store store1 = new org.framework.Services.PetstoreService.Store();
        Response response = store1.findThePurchaseOrderById(Routes.getOrder, orderId);
        response.then().log().all();
        System.out.println(response.asPrettyString());

    }

    @Test(priority = 8)
    public void ReturnsPetInventoriesByStatus() {
        org.framework.Services.PetstoreService.Store store = new org.framework.Services.PetstoreService.Store();
        Response response = store.ReturnsPetInventoriesByStatus(Routes.getInventoryByStatus);
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }

    @Test(priority = 9)
    public void AddNewPetInStore() throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/payloadTemplates/AddPetsInTheStore.json")));
        Pet p = new Pet();
        Response response = p.AddNewPetInStore(jsonString, Routes.postPet);
        response.then().log().all();
        System.out.println(response.asPrettyString());
        id = response.getBody().jsonPath().getString("id");
    }

    @Test(priority = 10)
    public void UpdatePetInStore() {

        String updatePetjson = "{\n" +
                "  \"id\": 20,\n" +
                "  \"category\": {\n" +
                "    \"id\": 10,\n" +
                "    \"name\": \"Cats\"\n" +
                "  },\n" +
                "  \"name\": \"doggie\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"https://images.pexels.com/photos/1851164/pexels-photo-1851164.jpeg?auto=compress&cs=tinysrgb&w=600\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"talkative\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";
        Pet p = new Pet();
        Response response = p.putPet(updatePetjson, Routes.postPet);
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }

    @Test(priority = 11)
    public void pet_findByStatus() {
        Pet pet = new Pet();
        //available, pending, sold
        Response response = pet.pet_FindByStatus(Routes.pet_findByStatus, "available");
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }

    @Test(priority = 12, dependsOnMethods = "placedThePetOrderInStore")
    public void pet_findByStatusByID() {
        Pet pet = new Pet();
        //available, pending, sold
        Response response = pet.FindPetByID(Routes.getPet, petId);
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }

    @Test(priority = 13, dependsOnMethods = "placedThePetOrderInStore")
    public void Updates_A_PetInTheStoreWithForm() {
        Pet pet = new Pet();
        Response response = pet.Updates_A_PetInTheStoreWithForm(Routes.Updates_A_PetInTheStoreWithForm, petId, "doggie", "available");
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }

    /*  @Test(priority = 14,dependsOnMethods = "placedThePetOrderInStore")
      public void uploadimageThePet() throws IOException{
          Pet pet=new Pet();
          String path = new String(Files.readAllBytes(Paths.get("D:\\Myprojects\\ApiTestingFrameworkImplementation\\src\\test\\resources\\api get.png")));
          Response response=pet.UploadImageToPet(Routes.UploadImageToPet,petId,path);
          response.then().log().all();
          System.out.println(response.asPrettyString());
      }*/
    @Test(priority = 14)
    public void logout() {
        User u = new User();
        Response response = u.Userlogout(Routes.userlogout);
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }

    @Test(priority = 15, dependsOnMethods = "LoginUser")
    public void DeleteUser() {
        User u = new User();
        Response response = u.deleteUser(Routes.deleteUser, UpdatedUsername);
        response.then().log().all();
        System.out.println(response.asPrettyString());

    }
    @Test(priority = 16,dependsOnMethods = "placedThePetOrderInStore")
    public void DeleteOrder() {
        org.framework.Services.PetstoreService.Store store=new org.framework.Services.PetstoreService.Store();
        Response response =store.DeleteOrder(Routes.deleteOrder,orderId);
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }
    @Test(priority = 17)
    public void DeletePetId(){
        Pet pet=new Pet();
        Response response=pet.DeletePetID(Routes.deletePetId,petId);
        response.then().log().all();
        System.out.println(response.asPrettyString());
    }


}
