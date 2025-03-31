# API Test Framework

This is an advanced API testing framework built with Java, Maven, and TestNG. It supports:
- **Dynamic test execution:** Reads API test cases from Excel.
- **API Testing:** Uses RestAssured for HTTP methods.
- **Database Operations:** JDBC utilities for database queries.
- **Logging:** Configured via Log4j2.
- **Reporting:** Generates detailed reports using ExtentReports.
- **Contract Testing:** Integrates with Pact for consumer-driven contracts.
- **Performance Testing:** Ability to run JMeter tests.
- **Mocking:** Uses WireMock to stub API endpoints.
- **CI/CD & Containerization:** Jenkins pipeline and Docker support.
- **Infrastructure Provisioning:** Terraform scripts to deploy test infrastructure.

## Project Structure

- **src/main/java:** Main framework code (base classes, configuration, utilities, reporting).
- **src/test/java:** Test classes.
- **src/resources:** Configuration and logging files.
- **terraform:** Terraform scripts for provisioning infrastructure.
- **pom.xml:** Maven build file.
- **testng.xml:** TestNG configuration.
- **Jenkinsfile:** CI/CD pipeline definition.
- **Dockerfile:** Docker build configuration.

## How to Run

1. **Maven Build & Test:**
   ```bash
   mvn clean test
