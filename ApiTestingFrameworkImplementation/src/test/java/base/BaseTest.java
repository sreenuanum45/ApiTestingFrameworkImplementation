

package base;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.framework.Utility.PayloadBuildUtil;
import org.framework.config.ConfigManager;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected RequestSpecification requestSpec;

    protected static ExtentReports extent;
    protected ExtentTest test;



    @BeforeSuite
    public void setupSuite() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent.attachReporter(spark);
        try {
            PayloadBuildUtil.setAllPayloads();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @BeforeMethod
    public void setupTest(Method method) {

    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }

}

