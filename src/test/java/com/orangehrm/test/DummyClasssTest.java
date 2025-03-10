package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClasssTest extends BaseClass {
	@Test
	public void dummyTest() {
		//Checking the test 
		// ExtentManager.startTest("Dummy Test 1"); --> Commenting because This has been
		// implemented in Test Listener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM") : "Test Failed - Title not matched";
		System.out.println("Test Passed - Title is matching");
		
		
        //ExtentManager.logSkip("This case is skipped");
         //to skip the test, uncomment below line
        //throw new SkipException("Skipping the test as part of Testing"); 
	}
	}


