package org.framework.Services.PetstoreService;

public class Routes {
    //base url
    public static final String baseURL = "https://petstore.swagger.io/v2";

    //user module endpoints
    public static final String postUser = baseURL + "/user";
    public static final String getUser = baseURL + "/user/{username}";
    public static final String PostUser_createWithArray = baseURL + "/user/createWithArray";
    public static final String putUser = baseURL + "/user/{username}";
    public static final String deleteUser = baseURL + "/user/{username}";
    public static final String loginUser = baseURL + "/user/login";
    public static final String userlogout= baseURL + "/user/logout";


    //store module endpoints
    public static final String postOrder = baseURL + "/store/order";
    public static final String getOrder = baseURL + "/store/order/{orderId}";
    public static final String deleteOrder = baseURL + "/store/order/{orderId}";
    public static  final String getInventoryByStatus = baseURL + "/store/inventory"; //ReturnsPetInventoriesByStatus

    //pet module endpoints
    public static final String postPet = baseURL + "/pet";
    public static final String getPet = baseURL + "/pet/{petId}";
    public static final String pet_findByStatus= baseURL + "/pet/findByStatus";
    public static final String Updates_A_PetInTheStoreWithForm = baseURL +  "/pet/{petId}";
    public static final String UploadImageToPet = baseURL + "/pet/{petId}/uploadImage";
    public static final String deletePetId= baseURL + "/pet/{petId}";
}
