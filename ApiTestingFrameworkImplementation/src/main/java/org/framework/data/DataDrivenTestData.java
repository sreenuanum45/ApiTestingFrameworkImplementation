package org.framework.data;

import java.lang.annotation.*;

public class DataDrivenTestData {
    @Data(name = "test_id")
    private String test_id;
    @Data(name = "scenario")
    private String scenario;
    @Data(name = "test_method")
    private String test_method;
    @Data(name = "test_data")
    private String test_data;
    @Data(name = "verification")
    private String verification;

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getScenario() {
        return scenario;
    }

    public String getMethod() {
        return test_method;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getTest_method() {
        return test_method;
    }

    public void setTest_method(String test_method) {
        this.test_method = test_method;
    }

    public String getTest_data() {
        return test_data;
    }

    public void setTest_data(String test_data) {
        this.test_data = test_data;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestData: iterationName: [");
        sb.append("test_id: ").append(test_id).append(", ");
        sb.append("scenario: ").append(scenario).append(", ");
        sb.append("test_method: ").append(test_method).append(", ");
        sb.append("test_data: ").append(test_data).append(", ");
        sb.append("verification: ").append(verification);
        sb.append("]");
        return sb.toString();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.METHOD})
    @Inherited
    public @interface Data {
        String name() default "";
    }



}
