package com.orangehrm.test;

import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;

import com.orangehrm.utilities.RetryAnalyzer;
import io.restassured.response.Response;

public class ApiTest {

	SoftAssert softassert = new SoftAssert();

	@Test
	public void verifyGetUserApi() {
		// Step 1: Define API EndPoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint:" + endPoint);

		// Step 2 : Send GET request
		ExtentManager.logStep("Sending GET request:");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step 3: Validate the status code
		ExtentManager.logStep("Validating the API response status code ");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		softassert.assertTrue(isStatusCodeValid, "Status code is not as expected");
		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForApi("Status code Validation passed");

		} else {
			ExtentManager.logFailureForApi("Status code Validation Failed");
		}

		// Step 4: Validate the username
		ExtentManager.logStep("Validating the response body : username ");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softassert.assertTrue(isUserNameValid, "UserName is not Valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationForApi("UserName Validation Passed");

		} else {
			ExtentManager.logFailureForApi("UserName Validation Failed");
		}

		// Step 5: Validate the Email
		ExtentManager.logStep("Validating the response body : Email ");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isUserEmailValid = "Sincere@april.biz".equals(userEmail);
		softassert.assertTrue(isUserEmailValid, "Email is not Valid");
		if (isUserEmailValid) {
			ExtentManager.logStepValidationForApi("Email Validation Passed");

		} else {
			ExtentManager.logFailureForApi("Email Validation Failed");
		}
		softassert.assertAll();
	}

}
