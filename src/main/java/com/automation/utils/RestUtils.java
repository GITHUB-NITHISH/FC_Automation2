package com.automation.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * RestUtils - Utility class for REST API testing
 */
public class RestUtils {
    private static final LoggerUtils logger = LoggerUtils.getInstance();

    /**
     * GET request
     */
    public static Response get(String endpoint) {
        logger.info("Making GET request to: " + endpoint);
        try {
            return RestAssured.get(endpoint);
        } catch (Exception e) {
            logger.error("GET request failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * POST request
     */
    public static Response post(String endpoint, Object body) {
        logger.info("Making POST request to: " + endpoint);
        try {
            return RestAssured.given()
                    .body(body)
                    .when()
                    .post(endpoint);
        } catch (Exception e) {
            logger.error("POST request failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * PUT request
     */
    public static Response put(String endpoint, Object body) {
        logger.info("Making PUT request to: " + endpoint);
        try {
            return RestAssured.given()
                    .body(body)
                    .when()
                    .put(endpoint);
        } catch (Exception e) {
            logger.error("PUT request failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * DELETE request
     */
    public static Response delete(String endpoint) {
        logger.info("Making DELETE request to: " + endpoint);
        try {
            return RestAssured.delete(endpoint);
        } catch (Exception e) {
            logger.error("DELETE request failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get response status code
     */
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    /**
     * Get response body as string
     */
    public static String getResponseBody(Response response) {
        return response.getBody().asString();
    }

    /**
     * Verify status code
     */
    public static boolean verifyStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        logger.info("Expected Status: " + expectedCode + ", Actual Status: " + actualCode);
        return actualCode == expectedCode;
    }
}
