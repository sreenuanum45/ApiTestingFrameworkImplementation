// src/test/java/reports/ExtentTestManager.java
package org.framework.reports;

import com.aventstack.extentreports.ExtentTest;
import org.framework.reports.ExtentManager;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
    private static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = ExtentManager.getInstance().createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }
}
