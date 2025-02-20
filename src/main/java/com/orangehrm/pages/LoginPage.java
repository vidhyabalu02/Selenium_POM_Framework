package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;

	// Defining all Locator of Login page using the "by" class
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[placeholder='Password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By invalidCredentialsError = By.xpath("//p[text()='Invalid credentials']");

	/*
	 * public LoginPage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver); /* In test class when login page is called the
	 * constructor of loginpage is invoked which automatically initializes the
	 * actionDriver class
	 *
	 * 
	 * }
	 */
	public LoginPage(WebDriver driver) {
		// Implementing singleton Design pattern by creating only one Action Class
		// instance
		this.actionDriver = BaseClass.getActionDriver();

	}

	// Login Functionality - Method to perform Login
	public void login(String userName, String password) {

		actionDriver.enterValue(userNameField, userName);
		actionDriver.enterValue(passwordField, password);
		actionDriver.click(loginButton);

	}

	// Method to check if the error message is there
	public boolean verifyErrorMessageisDisplayed() {
		return actionDriver.isDisplayed(invalidCredentialsError);
	}

	// Method to get text from error message
	public String getErrorMessageText() {
		return actionDriver.getValue(invalidCredentialsError);
	}

	// verify if errror message is correct or not
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(invalidCredentialsError, expectedError);
	}
}
