package org.framework.Utility;



import org.framework.data.DataDrivenTestData;
import org.framework.pojo.Store;
import org.framework.pojo.User;
import org.testng.annotations.DataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestDataProvider {

    private static final String TEST_DATA_FILE = "src/test/resources/testdata/users.json";
    private static final Logger LOGGER = Logger.getLogger(TestDataProvider.class.getName());

    @DataProvider(name = "userData")
    public static Object[][] getUserData() {
        try {
            // Read JSON file content
            String jsonContent = new String(Files.readAllBytes(Paths.get(TEST_DATA_FILE)));

            // Convert JSON to POJO array
            ObjectMapper mapper = new ObjectMapper();
            User[] users = mapper.readValue(jsonContent, User[].class);

            // Convert POJO array to DataProvider format
            Object[][] data = new Object[users.length][1];
            for (int i = 0; i < users.length; i++) {
                data[i][0] = users[i];
            }
            return data;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading test data file: " + TEST_DATA_FILE, e);
            return new Object[0][0]; // Return empty array if an error occurs
        }
    }
    // Updated TestDataProvider.testScenarioData data provider
    @DataProvider(name = "testScenarioData")
    public Object[][] testData(Method method) throws IOException {
        String methodName = method.getName();
        List<DataDrivenTestData> testDataList = ExcelReader.readTestDataFromExcel("src/test/resources/testdata/octopus.xlsx");
        List<DataDrivenTestData> methodData = new ArrayList<>();

        for (DataDrivenTestData data : testDataList) {
            // Corrected getter method from getMethod() to getTest_method()
            if (data.getTest_method().equalsIgnoreCase(methodName)) {
                methodData.add(data);
            }
        }

        Object[][] data = new Object[methodData.size()][1];
        for (int i = 0; i < methodData.size(); i++) {
            data[i][0] = methodData.get(i);
        }

        return data;
    }
    @DataProvider(name = "StoreData")  // Correct name
    public Object[][] getStoreDataFromJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFilePath = "/Users/yaseenkalo/Documents/automationProject/petStoreAutomation/TestData/store.json";  // Replace with the actual path

        List<Store> stores = objectMapper.readValue(new File(jsonFilePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Store.class));

        Object[][] data = new Object[stores.size()][6];  // 8 fields based on the User class

        for (int i = 0; i < stores.size(); i++) {
           Store store = stores.get(i);
            data[i][0] = store.getOrderId();
            data[i][1] = store.getPetId();
            data[i][2] = store.getQuantity();
            data[i][3] = store.getShipDate();
            data[i][4] = store.getStatus();
            data[i][5] = store.isComplete();
        }

        return data;
    }
    @DataProvider(name = "UserData")  // Correct name
    public Object[][] getUserDataFromJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFilePath = "src/test/resources/payloadTemplates/ListOfUsers.json";  // Replace with the actual path

        List<User> users = objectMapper.readValue(new File(jsonFilePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));

        Object[][] data = new Object[users.size()][8];  // 8 fields based on the User class

        for (int i = 0; i < users.size(); i++) {
           User user = users.get(i);
            data[i][0] = user.getUserId();
            data[i][1] = user.getUsername();
            data[i][2] = user.getFirstName();
            data[i][3] = user.getLastName();
            data[i][4] = user.getEmail();
            data[i][5] = user.getPassword();
            data[i][6] = user.getPhone();
            data[i][7] = user.getUserStatus();
        }

        return data;
    }
}
