package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {
	


	// Method to send the GET request
	public static Response sendGetRequest(String endpoint) {
		return RestAssured.get(endpoint);
	}

	// Method to send the POST request
	public static Response sendPostRequest(String endpoint, String payLoad) {
		return RestAssured.given().header("content-Type", "application/json").body(payLoad).post();

	}

	// Method to validate the response status
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode() == statusCode;

	}

	// Method to extract value from JSON response
	public static String getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);

	}

}
