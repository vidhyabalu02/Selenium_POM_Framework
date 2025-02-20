package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer {

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	//Triggered when the test starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		// Start logging in Extent report
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test Started" + testName);
	}

	// Triggered when a test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed successfully!",
					"Test End:" + testName + " -✔ Test Passed");
		} else 
		{
			ExtentManager.logStepValidationForApi("Test End:" + testName + " -✔ Test Passed");
		}
		
	}

	// Triggered when a Test fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStep(failureMessage);
			ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test End:" + testName + " -❌ Test Failed");
		} else {
			ExtentManager.logStep(failureMessage);
			ExtentManager.logFailureForApi("Test Failed!\", \"Test End:\" + testName + \" -❌ Test Failed");
		}
		
	}

	// Triggered when a Test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped!"+testName);
		
	}

	// Triggers when a suite starts
	@Override
	public void onStart(ITestContext context) {
		// Initialize the extent reports
		ExtentManager.getReporter();
	}

	// Triggers when a suite starts
	@Override
	public void onFinish(ITestContext context) {
		// Flush the extent report
		ExtentManager.endTest();	
	}

}
