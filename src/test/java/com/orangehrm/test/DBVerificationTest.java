package com.orangehrm.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

class DBVerificationTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());

	}
	
	@Test(dataProvider = "EmployeeVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String employeeid, String empname ) {
		
		SoftAssert softAssert = getSoftAssert();
		
		ExtentManager.logStep("Logging with  Credentials");
		loginPage.login(property.getProperty("dbusername"), property.getProperty("dbpassword"));
		
		ExtentManager.logStep("Click on PIM Tab ");
		homePage.clickOnPIMTab();
		
		ExtentManager.logStep("Search for Employee ");
		homePage.employeeSearch(empname);
	
		ExtentManager.logStep("Get the employee Name from DB ");
		String employee_id = employeeid;
		//fetch the Data into a Map
		
		Map<String,String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		String emplFirstName = employeeDetails.get("firstName");
		String empMiddleName = employeeDetails.get("middleName");
		String empLastName = employeeDetails.get("lastName");
		
		String empFirstandMiddleName = (emplFirstName+" "+empMiddleName).trim();
		ExtentManager.logStep("Verify the Employee First and MiddleName");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMidleName(empFirstandMiddleName),"First and Middle Name or not Matching");
		
		ExtentManager.logStep("Verify the Employee Last Name ");
		softAssert.assertTrue(homePage.verifyEmployeeLasstName(empLastName),"Last Name is not Matching");
		
		ExtentManager.logStep("DB Validation is complete ");
		
		softAssert.assertAll();
		
	}
	


}
