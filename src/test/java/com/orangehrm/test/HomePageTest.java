package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());

	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHrmLogo(String username, String password) {
		// ExtentManager.startTest("HomePage verify OrangeHRM logo Test");--> Commenting
		// because This has been implemented in Test Listener
		System.out.println("Running verify orangeHRm logo on thread " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login page entering uname and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHrmLogo(), "Test Case Filed - Orange HRM logo");
		ExtentManager.logStep("Logo Validation Successfull");
		ExtentManager.logStep("Logged Out Successfully");
	}

}