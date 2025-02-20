package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {

	private static final String FILE_PATH = System.getProperty("user.dir")+"/src/test/resources/testdata/TestData.xlsx";

	@DataProvider(name = "validLoginData") // Name userDefined
	public static Object[][] validLoginData() {
		return getSheetDataa("validLoginData"); // passing sheet Name
	}

	@DataProvider(name = "InvalidLoginData")
	public static Object[][] invalidLoginData() {
		return getSheetDataa("InvalidLoginData");
	}
	
	@DataProvider(name="EmployeeVerification")
	public static Object[][] employeeVerification() {
		return getSheetDataa("employeeverification");
	}
	
	

	private static Object[][] getSheetDataa(String sheetName) {

		List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);
		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];
		for (int i = 0; i < sheetData.size(); i++) {
			data[i] = sheetData.get(i);
		}
		return data;

	}
}
