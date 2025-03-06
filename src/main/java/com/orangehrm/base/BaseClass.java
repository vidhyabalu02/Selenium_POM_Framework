package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties property;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	protected static ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Configuration file - Load the file by giving the config.properties file
		property = new Properties();
		FileInputStream file = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
		property.load(file);
		logger.info("config.properties file loaded");
		// Start the Extent Report
		// ExtentManager.getReporter(); --> Commenting because This has been implemented
		// in Test Listener

	}

	@BeforeMethod
	public synchronized void setUp() throws IOException {
		System.out.println("Setting up webDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		logger.info("WebDriver Initialized and Browser maximised");
		logger.trace("This is a Trace Message");
		logger.error("This is a Error Message");
		logger.debug("This is a Degbug Message");
		logger.fatal("This is a Fatal Message");
		logger.warn("This is a Warn Message");

		/*
		 * To initialize the ActionDriver object only once if(actionDriver == null) {
		 * actionDriver = new ActionDriver(driver);
		 * logger.info("ActonDriver Instance is created"+Thread.currentThread().getId())
		 * ; }
		 */

		// Initialize ActionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for the thread :" + Thread.currentThread().getId());
	}

	/*
	 * Initialize the webDriver based on the configuration given in
	 * config.properties file
	 */
	private synchronized void launchBrowser() {

		String browser = property.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			// driver = new ChromeDriver();
			
			//Creating chrome options
			
			ChromeOptions options = new ChromeOptions();
			//options.addArguments("--headless"); //This runs chrome in headless mode
			options.addArguments("--disable-gpu"); //This disable GPu for headless mode
			//options.addArguments("--window-size=1920,1080"); //this sets the window size
			options.addArguments("--disable-notifications"); //disable browser notification
			options.addArguments("--no-sandbox"); //required for some CI environments
			options.addArguments("--disable-dev-shm-usage"); //ResolveF issues in resources
			//options.addArguments("--ignore-ssl-errors=yes");
			//options.addArguments("--ignore-certificate-errors");
			
			driver.set(new ChromeDriver(options)); // New change for threadlocal
			ExtentManager.registerWebDriver(getDriver());
			logger.info("ChromeDriver Instance is created ");
		} else if (browser.equalsIgnoreCase("edge")) {
			
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); //This runs edge browser in headless mode
			options.addArguments("--disable-gpu"); //This disable GPu for headless mode
			options.addArguments("--window-size=1920,1080"); //this sets the window size
			options.addArguments("--disable-notifications"); //disable browser notification
			options.addArguments("--no-sandbox"); //required for some CI environments
			options.addArguments("--disable-dev-shm-usage"); //ResolveF issues in resources
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver(options));
			ExtentManager.registerWebDriver(getDriver());
			logger.info("EdgeDriver Instance is created ");
		} else if (browser.equalsIgnoreCase("firefox")) {
			
			FirefoxOptions options = new FirefoxOptions();
			
			options.addArguments("--headless"); //This runs Firefox in headless mode
			options.addArguments("--disable-gpu"); //This disable GPu for headless mode
			options.addArguments("--width=1920,1080"); //this sets the window width
			options.addArguments("--height=1080");//this sets the window height
			options.addArguments("--disable-notifications"); //disable browser notification
			options.addArguments("--no-sandbox"); //required for some CI environments
			options.addArguments("--disable-dev-shm-usage"); //ResolveF issues in resources
			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options));
			ExtentManager.registerWebDriver(getDriver());
			logger.info("FirefoxDriver Instance is created ");
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
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// To maximize the browser
		driver.get().manage().window().maximize();

		// To navigate to the URL
		try {
			driver.get().get(property.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Url Navigation -  Failed to navigate to the URL" + e.getMessage());
		}

	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Browser Quit - Not able to Quit" + e.getMessage());
			}
		}

		logger.info("WebDriver & ActionDriver instance  is closed");
		/*
		 * driver = null; actionDriver = null;
		 */
		driver.remove();
		actionDriver.remove();
		// ExtentManager.endTest(); --> Commenting because This has been implemented in
		// Test Listener
	}

	/*
	 * public WebDriver getDriver() { // Driver getter method return driver;
	 * 
	 * }
	 */

	// Getter Method fo webDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();

	}

	// Getter Method for Action Driver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");

		}
		return actionDriver.get();

	}

	//Getter method for soft assertion
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	public void setDriverr(ThreadLocal<WebDriver> driver) {
		// Driver setter method
		this.driver = driver;
	}

	// Getter method for properties
	public static Properties getProp() {
		return property;
	}

	public void staticWait(int seconds) {
		// static wait for pause
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));

	}
}
