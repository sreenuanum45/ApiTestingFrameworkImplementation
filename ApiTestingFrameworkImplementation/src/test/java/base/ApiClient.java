
package base;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;


public class ApiClient {

    private static RequestSpecification requestSpec;

    public static RequestSpecification getRequestSpecification() {
        if (requestSpec == null) {
            String baseURI = org.framework.config.ConfigManager.getProperty("baseURI");
            requestSpec = new RequestSpecBuilder()
                    .setBaseUri(baseURI)
                    .addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter())
                    .build();
            RestAssured.requestSpecification = requestSpec;
        }
        return requestSpec;
    }
    public static void setRequestSpecification(RequestSpecification requestSpec) {
        ApiClient.requestSpec = requestSpec;
    }
    public static void resetRequestSpecification() {
        requestSpec = null;
    }
    

}
