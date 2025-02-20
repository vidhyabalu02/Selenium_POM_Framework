package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClasssTest2 extends BaseClass {
	@Test
	public void dummyTest2() {
		// ExtentManager.startTest("Dummy Test 2");--> Commenting because This has been
		// implemented in Test Listener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM") : "Test Failed - Title not matched";
		System.out.println("Test Passed - Title matched");
		ExtentManager.logStep(" Validation Successfull");
	}

}
