package org.framework.Utility;

import org.testng.annotations.DataProvider;

import java.sql.*;
import java.util.*;

public class DBUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/api_tests";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static List<Map<String, String>> getTestData(String query) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> data = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    data.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                dataList.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
        return dataList;
    }
    
    @DataProvider(name = "dbTestData")
    public Object[][] getDatabaseTestData() {
        List<Map<String, String>> testDataList = DBUtils.getTestData("SELECT * FROM test_cases");
        return testDataList.stream().map(data -> new Object[]{data}).toArray(Object[][]::new);
    }

}
