package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {

	public static List<String[]> getSheetData(String filePath, String sheetName) {

		// Data variable is defined as list of array of string
		List<String[]> data = new ArrayList<>();

		//
		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Sheet" + sheetName + "does not exist");
			}
			// Iterate through the rows
			for (Row row : sheet) {

				if (row.getRowNum() == 0) {
					continue;
				}
				// Read all the cells in the row
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getCellValue(cell));
				}
				// convert Rowdata to String[]
				data.add(rowData.toArray(new String[0]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			}
			return String.valueOf((int) cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}

	}

}
