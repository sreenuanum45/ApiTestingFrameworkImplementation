package org.framework.Utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.framework.data.DataDrivenTestData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {
    private static final String FILE_PATH = "src/test/testdata/api_test_cases.xlsx";

    public static List<Map<String, String>> getTestData(String sheetName) {
        List<Map<String, String>> testDataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                Map<String, String> testCaseData = new HashMap<>();

                for (int cellNum = 0; cellNum < headerRow.getLastCellNum(); cellNum++) {
                    String key = headerRow.getCell(cellNum).getStringCellValue();
                    String value = row.getCell(cellNum) != null ? row.getCell(cellNum).toString() : "";
                    testCaseData.put(key, value);
                }
                testDataList.add(testCaseData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
        }
        return testDataList;
    }
    public static List<String[]> getDataFromExcel(String filePath, String sheetName) {
        List<String[]> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            int lastRow = sheet.getLastRowNum();
            for (int i = 1; i <= lastRow; i++) {  // assuming first row is header
                Row row = sheet.getRow(i);
                int cells = row.getLastCellNum();
                String[] rowData = new String[cells];
                for(int j=0; j<cells; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = (cell == null) ? "" : cell.toString();
                }
                data.add(rowData);
            }
            LoggerUtil.info("Read " + data.size() + " rows from Excel: " + filePath);
        } catch(Exception e) {
            LoggerUtil.error("Error reading Excel file: " + filePath, e);
        }
        return data;
    }
    // Updated ExcelReader.readTestDataFromExcel method
    public static List<DataDrivenTestData> readTestDataFromExcel(String excelFilePath) throws IOException {
        List<DataDrivenTestData> testDataList = new ArrayList<>();
        File file = new File(excelFilePath);
        if (!file.exists()) {
            throw new RuntimeException("Excel file not found at: " + excelFilePath);
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // Skip empty rows

                DataDrivenTestData data = new DataDrivenTestData();

                // Process each cell with null checks and DataFormatter
                data.setTest_id(getCellValueAsString(row, 0, formatter));
                data.setScenario(getCellValueAsString(row, 1, formatter));
                data.setTest_method(getCellValueAsString(row, 2, formatter));
                data.setTest_data(getCellValueAsString(row, 3, formatter));
                data.setVerification(getCellValueAsString(row, 4, formatter));

                testDataList.add(data);
            }
        }
        return testDataList;
    }

    private static String getCellValueAsString(Row row, int cellIndex, DataFormatter formatter) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return cell != null ? formatter.formatCellValue(cell) : "";
    }
}