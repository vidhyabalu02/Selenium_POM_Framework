package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Method to Initialize the Extent Reports
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReports/ExtentReports.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("AutomationTestReport");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.STANDARD);
			// spark.config().setTheme(Theme.DARK);
			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding systemInformation
			extent.setSystemInfo("OperatingSystem", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("Java.verison"));
			extent.setSystemInfo("UserName", System.getProperty("user.name"));

		}
		return extent;

	}

	// Method to start the test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;

	}

	// Method to End the Test
	public synchronized static void endTest() {
		getReporter().flush();
	}

	// Method to get current thread's test
	public synchronized static ExtentTest getTest() {
		return test.get();

	}

	// Method to get the name of currentTest
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No Test is currently active for this thread";
		}
	}

	// Log a step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	// Log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
		getTest().pass(logMessage);
		// Screenshot Method
		attachScreenshot(driver, screenShotMessage);

	}

	// Log a step validation for API
	public static void logStepValidationForApi(String logMessage) {
		getTest().pass(logMessage);

	}

	// Log a Failure for UI
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
		String colormessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colormessage);
		attachScreenshot(driver, screenShotMessage);

	}

	// Log a Failure for API
	public static void logFailureForApi(String logMessage) {
		String colormessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colormessage);
	}

	// Log a skip
	public static void logSkip(String logMessage) {
		String colormessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colormessage);

	}

	// Take a screenshot with date and time in the file
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		// Format date and time for file
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		// Saving the screenshot to a file
		String destinationPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenshotName
				+ "_" + timeStamp + ".png";
		File finalPath = new File(destinationPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Convert the screenshot to base64 fir embedding in the report

		String base64Format = convertToBase64(src);
		return base64Format;

	}

	// Method to convert the screenshot to Base64 format
	public static String convertToBase64(File ScreenShotFile) {

		String base64Format = "";

		// Read the file content into a byte array
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(ScreenShotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Convert the byte array to Base64 String
		return base64Format;

	}

	// Method to Attach Screenshot to the report using base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenshotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			getTest().fail("Failed to attach screenshot");
			e.printStackTrace();
		}

	}

	// Register WebDriver for current thread
	public static void registerWebDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);

	}
}
