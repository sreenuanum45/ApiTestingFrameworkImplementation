//package tests;
//
//
//import io.restassured.response.Response;
//import org.framework.Utility.ExcelReader;
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
///*import utils.ExcelReader;
//import utils.RestUtils;
//import utils.LoggerUtil;*/
//
//import javax.json.JsonReader;
//import java.util.List;
//import java.util.Map;
//
//public class DynamicApiTests {
//
//    @DataProvider(name = "apiTestDataFromExcel")
//    public Object[][] getTestData() {
//        List<Map<String, String>> testDataList = ExcelReader.getTestData("API_Tests");
//        return testDataList.stream().map(data -> new Object[]{data}).toArray(Object[][]::new);
//    }
//    /**
//     * DataProvider that reads test data from a JSON file.
//     * Expects JsonReader.getTestData(String filePath) to return a List of Maps.
//     *
//     * @return Object[][] containing test data from the JSON file "API_Tests.json".
//     */
//    @DataProvider(name = "apiTestDataFromJson")
//    public Object[][] getTestDataFromJson() {
//        List<Map<String, String>> testDataList = ExcelReader.getTestData("API_Tests.json");
//        return testDataList.stream()
//                .map(data -> new Object[]{data})
//                .toArray(Object[][]::new);
//    }
//    /**
//     * DataProvider that reads test data from a CSV file.
//     * Expects CSVReader.getTestData(String filePath) to return a List of Maps.
//     *
//     * @return Object[][] containing test data from the CSV file "API_Tests.csv".
//     */
//    @DataProvider(name = "apiTestDataFromCSV")
//    public Object[][] getTestDataFromCSV() {
//        List<Map<String, String>> testDataList = ExcelReader.getTestData("API_Tests.csv");
//        return testDataList.stream()
//                .map(data -> new Object[]{data})
//                .toArray(Object[][]::new);
//    }
//    /**
//     * DataProvider that supplies manual (inline) test data.
//     *
//     * @return Object[][] containing hard-coded test data maps.
//     */
//    @DataProvider(name = "manualTestData")
//    public Object[][] getManualTestData() {
//        return new Object[][]{
//                {Map.of("username", "user1", "password", "pass1")},
//                {Map.of("username", "user2", "password", "pass2")}
//        };
//    }
//
//
//
//   /* @Test(dataProvider = "apiTestData")
//    public void executeApiTest(Map<String, String> testData) {
//        LoggerUtil.info("Executing Test: " + testData.get("Test Title/Scenario"));
//
//        Response response = RestUtils.executeApiRequest(testData);
//
//        int actualStatusCode = response.getStatusCode();
//        int expectedStatusCode = Integer.parseInt(testData.get("Expected Status Code"));
//        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status Code Mismatch!");
//
//        LoggerUtil.info("Test Passed for: " + testData.get("API Endpoint"));
//    }*/
//  /*  @Test(dataProvider = "apiTestData")
//    public void executeApiWithChaining(Map<String, String> testData) {
//        Response response = RestUtils.executeApiRequest(testData);
//        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(testData.get("Expected Status Code")));
//
//        if (testData.get("Test Title/Scenario").equals("Create User")) {
//            int userId = response.jsonPath().getInt("id");
//            testData.put("API Endpoint", "/users/" + userId);
//            testData.put("Request Method", "GET");
//            executeApiTest(testData);
//        }
//
//    }*/
//
//
//}
