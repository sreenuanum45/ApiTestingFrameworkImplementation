package org.framework.Utility;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

public class JsonDataReader {

    // Generic method to read a JSON file and return a list of objects of the given type.
    public static <T> List<T> readJsonData(String filePath, Class<T[]> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            T[] data = mapper.readValue(new File(filePath), clazz);
            return List.of(data);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   /* @DataProvider(name = "jsonTestData")
    public Object[][] jsonTestData() {
        List<ApiTestCase> testCases = JsonDataReader.readJsonData(
                "src/test/testdata/api_test_cases.json", ApiTestCase[].class);
        Object[][] data = new Object[testCases.size()][1];
        for (int i = 0; i < testCases.size(); i++) {
            data[i][0] = testCases.get(i);
        }
        return data;
    }*/

}
