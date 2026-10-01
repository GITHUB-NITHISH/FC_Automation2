package com.automation.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ApiRequestBuilder {

    private static String token;

    public static void setAuthToken(String authToken) {
        token = authToken;
    }



    public static RequestSpecification getRequest() {

           RequestSpecification request = RestAssured.given()
                   .baseUri(ConfigurationManager.getApiBaseUrl())
                   .header("Content-Type", "application/json")
                   .header("Accept", "application/json");

           if (token != null && !token.isEmpty()) {
               request.header("Authorization", token);
           }

           return request;
       }

}


