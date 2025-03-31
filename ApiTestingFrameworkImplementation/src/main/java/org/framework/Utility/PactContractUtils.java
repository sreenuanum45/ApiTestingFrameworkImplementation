package org.framework.Utility;

public class PactContractUtils {

    // Example using a JUnit rule (can be adapted for your needs)
/*    @Rule
    public WireMockRule mockProvider = new WireMockRule(8081);

    public PactDslWithProvider createPact(PactDslWithProvider builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("id", "123")
                .stringType("name", "John Doe");

        return builder
                .given("User exists")
                .uponReceiving("A request for user details")
                .path("/user/123")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body);
    }*/
}
