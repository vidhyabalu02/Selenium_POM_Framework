package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseClass {

	protected static Properties property;
	protected static WebDriver driver;

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Configuration file - Load the file by giving the config.properties file
		property = new Properties();
		FileInputStream file = new FileInputStream("src/main/resources/config.properties");
		property.load(file);

	}

	@BeforeMethod
	public void setUp() throws IOException {
		System.out.println("Setting up webDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);

	}

	/*
	 * Initialize the webDriver based on the configuration given in
	 * config.properties file
	 */
	private void launchBrowser() {

		String browser = property.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {
			throw new IllegalArgumentException("Browser Not Supported" + browser);
		}
	}

	/*
	 * Configure the browser - Implicit wait, Maximize the browser window and
	 * Navigate to the URL
	 */
	public void configureBrowser() {
		// Implicit Wait implementation
		int implicitWait = Integer.parseInt(property.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// To maximize the browser
		driver.manage().window().maximize();

		// To navigate to the URL
		try {
			driver.get(property.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Url Navigation -  Failed to navigate to the URL");
		}

	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("Browser Quit - Not able to Quit");
			}
		}

	}

	public WebDriver getDriver() {
		// Driver getter method
		return driver;
	}

	// Getter method for properties
	public static Properties getProp() {
		return property;
	}

	public void setDriverr(WebDriver driver) {
		// Driver setter method
		this.driver = driver;
	}

	public void staticWait(int seconds) {
		// static wait for pause
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));

	}
}
