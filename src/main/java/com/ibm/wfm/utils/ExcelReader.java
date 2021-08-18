package com.ibm.wfm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A simplistic class that provides static utility methods to reads an Excel file.
 * 
 * @author ponessa@us.ibm.com
 *
 */
public class ExcelReader {

	public static void main(String[] args) throws IOException {
		String excelFilePath = "/Users/steve/Downloads/Shortlist publications/GBS Taxonomy - Shortlist/M306 Short List.xlsx";
		String tabName = "GBS Short List";
		ArrayList<String[]> mySheet = ExcelReader.readExcelPage(excelFilePath, tabName);
		System.out.println("Complete");
	}

	public static ArrayList<String[]> readExcelPage(String excelFilePath) throws IOException {
		return readExcelPage(excelFilePath, null);
	}
	
	public static ArrayList<String[]> readExcelPage(String excelFilePath, String tabName) throws IOException {
		ArrayList<String[]> rowArray = null;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = null;
		if (tabName == null)
			sheet = workbook.getSheetAt(0);
		else
			sheet = workbook.getSheet(tabName);
		
		if (sheet==null) {
			System.out.println("Sheet "+ tabName + " not found in spreadsheet "+excelFilePath);
			return null;
		}
		
		// Iterator<Row> iterator = sheet.iterator();

		// while (iterator.hasNext()) {
		for (Row row : sheet) {
			// Row nextRow = iterator.next();
			//Iterator<Cell> cellIterator = row.cellIterator();
			
			String[] values = new String[row.getLastCellNum()];
			
			//while (cellIterator.hasNext()) {
				//Cell cell = cellIterator.next();
			for(int cn=0; cn<row.getLastCellNum(); cn++) {
				// If the cell is missing from the file, generate a blank one
				// (Works by specifying a MissingCellPolicy)
				Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					values[cn]=cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					values[cn]=String.valueOf(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					values[cn]=String.valueOf(cell.getNumericCellValue());
					break;
				//case _NONE:
				//	values[cn]="";
				//	break;
				case Cell.CELL_TYPE_BLANK:
					values[cn]="";
					break;
				case Cell.CELL_TYPE_ERROR:
					values[cn]="<error>";
					System.out.println("Cell Error: "+String.valueOf(cell.getErrorCellValue()));
					break;
				case Cell.CELL_TYPE_FORMULA:
					//values[cn]="<formula - "+cell.getCellFormula()+">";	
					//System.out.println("Cell Forumula: "+cell.getCellFormula());

				    switch (cell.getCachedFormulaResultType()) {
				        case Cell.CELL_TYPE_BOOLEAN:
				        	values[cn]=String.valueOf(cell.getBooleanCellValue());
				            break;
				        case Cell.CELL_TYPE_NUMERIC:
				        	values[cn]=String.valueOf(cell.getNumericCellValue());
				            break;
				        case Cell.CELL_TYPE_STRING:
				        	values[cn]=String.valueOf(cell.getRichStringCellValue());
				            break;
				    }

					break;
				default:
					values[cn]="<unknown cell type="+cell.getCellType()+">";
					System.out.println("Cell unknown type: "+cell.getCellType());
					break;
				} //end - switch (cell.getCellType())
			} //end - for(int cn=0; cn<row.getLastCellNum(); cn++)
			
			if (rowArray==null) rowArray = new ArrayList<String[]>(sheet.getLastRowNum());
			rowArray.add(values);
			
		} //end - for (Row row : sheet)

		workbook.close();
		inputStream.close();
		
		return rowArray;
	}

}