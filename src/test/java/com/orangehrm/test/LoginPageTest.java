package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());

	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		// ExtentManager.startTest("Valid Login Test");--> Commenting because This has
		// been implemented in Test Listener
		System.out.println("Running verify valid login on thread " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page entering uname and password");
		loginPage.login(username,  password);
		ExtentManager.logStep("Verifying admin tab is visible or not");
		Assert.assertTrue(homePage.verifyAdminTabisDisplayed(), "Admin tab should be visible after successfull login ");
		ExtentManager.logStep("Validation Successfull");
		homePage.logout();
		ExtentManager.logStep("Logged Out Successfully");
		staticWait(2);
	}

	@Test(dataProvider = "InvalidLoginData", dataProviderClass = DataProviders.class)
	public void verifyInvalidLoginTest(String username, String password) {
		// ExtentManager.startTest("In-valid Login Test");--> Commenting because This
		// has been implemented in Test Listener
		System.out.println("Running verify invalid login on thread " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page entering uname and password");
		loginPage.login(username,  password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),
				"Test Failed - Invalid error message not matching");
		ExtentManager.logStep("Invalid login Validation Successfull");
		ExtentManager.logStep("Logged Out Successfully");

	}

}
