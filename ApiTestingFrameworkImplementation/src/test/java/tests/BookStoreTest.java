package tests;

import base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.Services.Account;
import org.framework.Services.BookStore;
import org.framework.Utility.PayloadBuildUtil;
import org.framework.Utility.TestDataProvider;
import org.framework.config.ConfigManager;
import org.framework.data.DataDrivenTestData;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.path.json.JsonPath.from;

public class BookStoreTest extends BaseTest {
    public String json;
    public String token;
    public String userID;
    public List<String> isbns;
    public String isbn;

    @BeforeClass(alwaysRun = true)
    public void BeforeClassSetup() {
        json = PayloadBuildUtil.getDPSPayload();
    }

    @Test(priority = 1, dataProvider = "testScenarioData", dataProviderClass = TestDataProvider.class)
    public void CreateUser(DataDrivenTestData data) throws Exception {
        {
            final HashMap<String, Object> tdMap = from(data.getTest_data()).get();
            final HashMap<String, Object> VdMap = from(data.getVerification()).get();

            Account account = new Account(ConfigManager.getProperty("baseURI"), "");
            Response response = account.CreateAccount("/Account/v1/User", json);
            int statusCode = response.getStatusCode();
            System.out.println(statusCode);
            System.out.println("=======");
            System.out.println(response.getBody().prettyPrint());
            System.out.println("=======");
            userID = response.getBody().jsonPath().getString("userID");

        }
    }

    @Test(priority = 2)
    public void GenerateToken() throws Exception {
        {
            Account account = new Account(ConfigManager.getProperty("baseURI"), "");
            Response response = account.GenerateToken("/Account/v1/GenerateToken", json);
            int statusCode = response.getStatusCode();
            System.out.println(statusCode);
            System.out.println("=======");
            System.out.println(response.asPrettyString());
            System.out.println("=======");
            token = response.getBody().jsonPath().getString("token");

        }
    }

    @Test(priority = 3)
    public void AuthenticateUser() throws Exception {
        Account account = new Account(ConfigManager.getProperty("baseURI"), "");
        Response response = account.AuthenticateUser("/Account/v1/Authorized", json);
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
    }

    @Test(priority = 4, dependsOnMethods = "CreateUser")
    public void GetAccountDetails() throws Exception {
        Account account = new Account(ConfigManager.getProperty("baseURI"), "");
        Response response = account.GetAccountDetails("/Account/v1/User/" + userID);
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
    }

    @Test(priority = 5)
    public void GetBooks() throws Exception {
        BookStore bookStore = new BookStore(ConfigManager.getProperty("baseURI"), "");
        Response response = bookStore.GetAllBookDetails("/BookStore/v1/Books");
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
        isbns = response.getBody().jsonPath().getList("books.isbn");
    }

    @Test(priority = 6, dependsOnMethods = {"AuthenticateUser"})
    public void AddListOfBooks() throws Exception {
        String addbooks = "{\n" +
                "  \"userId\": \"" + userID + "\",\n" +
                "  \"collectionOfIsbns\": [\n" +
                "    {\n" +
                "      \"isbn\": \"9781449325862\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        BookStore bookStore = new BookStore(ConfigManager.getProperty("baseURI"), "");
        Response response = bookStore.addListOfBooks("/BookStore/v1/Books", addbooks,token);
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
        isbn = response.getBody().jsonPath().getString("books[0].isbn");
    }

    @Test(priority = 7)
    public void getBookDetails() throws Exception {
        BookStore bookStore = new BookStore(ConfigManager.getProperty("baseURI"), "");
        Response response = bookStore.getBook("/BookStore/v1/Book", token, isbns.get(0));
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
    }

    @Test(priority = 8)
    public void UpdateBookDetails() throws Exception {
        BookStore bookStore = new BookStore(ConfigManager.getProperty("baseURI"), "");
        String replacedisbn = "{\r\n" +
                "  \"userId\": \"" + userID + "\",\r\n" +
                "  \"isbn\": \"9781449325862\"\r\n" +
                "}";
        Response response = bookStore.updateISBN("/BookStore/v1/Books/", replacedisbn, token, isbn);
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
    }

    @Test(priority = 9)
    public void deleteBook() throws Exception {
        System.out.println(isbns.get(2));
        String deletejson = "{\n" +
                "  \"userId\": \"" + userID + "\",\n" +
                "  \"isbn\": \"" + isbns.get(2) + "\"\n" +
                "}";
        BookStore bookStore = new BookStore(ConfigManager.getProperty("baseURI"), "");
        Response response = bookStore.deleteBook("/BookStore/v1/Book", token, deletejson);
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
    }

    @Test(priority = 10)
    public void deleteBooks() throws Exception {
        BookStore bookStore = new BookStore(ConfigManager.getProperty("baseURI"), "");
        Response response = bookStore.DeleteBooks("/BookStore/v1/Books" , token,userID);
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
    }

    @Test(priority = 11)
    public void DeleteUser() throws Exception {
        Account account = new Account(ConfigManager.getProperty("baseURI"), "");
        Response response = account.deleteUser("/Account/v1/User/" + userID, token);
        System.out.println("=======");
        System.out.println(response.asPrettyString());
        System.out.println("=======");
    }
}
