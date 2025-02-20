package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {

	private ActionDriver actionDriver;

	// Defining all Locator of Home page using the "by" class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIdButton = By.className("oxd-userdropdown-icon");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHrmlogo = By.xpath("//div[@class = 'oxd-brand-banner']//img");
	
	// you can use xpath like this as well //div[@class =
	// 'oxd-brand-banner']//child::img
	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeName = By
			.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By SearchButton = By.xpath("//button[@type='submit']");
	private By empFirstandMidName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By empLastName=By.xpath("//div[@class='oxd-table-card']/div/div[4]");


	/*
	 * public HomePage(WebDriver driver) { //Initialize the ActionDriver object by
	 * passing the WebDriver Instance this.actionDriver = new ActionDriver(driver);
	 * 
	 * }
	 */
	public HomePage(WebDriver driver) {
		// Implementing singleton Design pattern by creating only one Action Class
		// instance
		this.actionDriver = BaseClass.getActionDriver();

	}

	// Verify if admin tab is visible
	public boolean verifyAdminTabisDisplayed() {
		return actionDriver.isDisplayed(adminTab);
	}

	// verify if orangeHrm logo is present
	public boolean verifyOrangeHrmLogo() {
		return actionDriver.isDisplayed(orangeHrmlogo);
	}

	// Method to perform logout operation
	public void logout() {
		actionDriver.click(userIdButton);
		actionDriver.click(logoutButton);
	}

	//Method to Navigate to PIM tab 
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}
	
	//Employee search 
	public void employeeSearch(String value) {
		actionDriver.enterValue(employeeName, value);
		actionDriver.click(SearchButton);
		actionDriver.scrollToElement(empFirstandMidName);
		
	}
	
	//verify Employee first and Middle Name 
	public boolean verifyEmployeeFirstAndMidleName(String empFirstandMiddleNamefromDB) {
		return actionDriver.compareText(empFirstandMidName, empFirstandMiddleNamefromDB);
	}
	
	//verify Employee last Name 
		public boolean verifyEmployeeLasstName(String empLastNamefromDB) {
			return actionDriver.compareText(empLastName, empLastNamefromDB);
		}
}
