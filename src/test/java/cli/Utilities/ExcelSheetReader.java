package cli.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelSheetReader {

	private XSSFWorkbook workbook;
	private XSSFCell cell;
	private XSSFRow row;
	private FileInputStream fis = null;
	public static HashMap<String, HashMap<String, String>> TestData;

	public ExcelSheetReader() {
		String fileName=Constants.datasheetPath+"Datasheet.xlsx";
		try {
			
			fis = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(fis);
		} catch (FileNotFoundException fnfEx) {
			System.out.println(fileName + " is not Found. please check the file name.");
			System.exit(0);
		} catch (IOException ioEx) {
			System.out.println(fis + " is not Found. please check the path.");
		} catch (Exception ex) {
			System.out.println("There is error reading/loading xls file, due to " + ex);
		}
	}

	public int searchField(String sheetName, int colNum, String value) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			int rowCount = sheet.getLastRowNum();
			for (int i = 0; i <= rowCount; i++) {
				if (getCellData(sheetName, i, colNum).equalsIgnoreCase(value)) {
					return i;
				}
			}
			return -1;
		} catch (Exception e) {
			throw (e);
		}
	}

	public int getColHeaderNum(String sheetName, String value) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			int rowCount = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i <= rowCount; i++) {
				if (getCellData(sheetName, 0, i).equalsIgnoreCase(value)) {
					return i;
				}
			}
			return -1;
		} catch (Exception e) {
			throw (e);
		}
	}

	public String[] getRowData(String sheetName, int rowNum) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		String[] temp = new String[sheet.getRow(rowNum).getLastCellNum()];
		for (int i = 0; i < temp.length; i++)
			temp[i] = getCellData(sheetName, rowNum, i);
		return temp;
	}

	public String getCellData(String sheetName, int rowNum, int colNum) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			cell = sheet.getRow(rowNum).getCell(colNum);
			
			if (cell == null)
				return "";
			if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC || cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
			
		} catch (Exception e) {
			return "";
		}
	}

	public int getRowNum(String sheetName, String rowValue) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			for (int i = 0; i <= getLastRowNum(sheetName); i++) {
				if (sheet.getRow(i).getCell(0).getStringCellValue().equals(rowValue))
					return i;
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int getRowNum(String sheetName, String rowValue, int colNum) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			for (int i = 0; i <= getLastRowNum(sheetName); i++) {
				if (sheet.getRow(i).getCell(colNum).getStringCellValue().equals(rowValue))
					return i;
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getColNum(String sheetName, String colValue) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			row = sheet.getRow(0);
			int lastColNum = row.getLastCellNum();
			for (int i = 0; i <= lastColNum; i++) {
				if (row.getCell(i).getStringCellValue().equals(colValue))
					return i;
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getColNum(String sheetName, String colValue, int rowNum) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			row = sheet.getRow(rowNum);
			for (int i = 0; i <= getLastRowNum(sheetName); i++) {
				if (row.getCell(i).getStringCellValue().equals(colValue))
					return i;
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}

	public String getCellIntData(String sheetName, int rowNum, int colNum) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			cell = sheet.getRow(rowNum).getCell(colNum);
			String CellData = String.valueOf(cell.getNumericCellValue());
			CellData = CellData.substring(0, CellData.indexOf("."));
			return CellData;
		} catch (Exception e) {
			return "";
		}

	}

	public String getCellData(String sheetName, String colName, int rowNum) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			if (rowNum <= 0)
				return "";
			int col_Num = -1;
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return "";
			row = sheet.getRow(rowNum);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);
			if (cell == null)
				return "";
			if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC || cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	public String getCellData1(String sheetName, int rowNum, int colNum) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		try {
			if (rowNum < 0)
				return "";
			row = sheet.getRow(rowNum);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";
			if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC || cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	public List<HashMap<String, String>> getExcelData(String sheetName) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		int lastRow = sheet.getLastRowNum();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>(lastRow);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			HashMap<String, String> testdata = new HashMap<String, String>();
			for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++)
				testdata.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
			result.add(testdata);
		}
		return result;
	}

	public HashMap<String, String> getRowTestData(String sheetName, String testCaseName) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		HashMap<String, String> testdata = new HashMap<String, String>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			if (sheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(testCaseName)) {
				for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++)
					testdata.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
				break;
			}
		}
		return testdata;
	}

	public HashMap<String, String> getRowTestData(String sheetName, String testCaseName, int colNum) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		HashMap<String, String> testdata = new HashMap<String, String>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			if (sheet.getRow(i).getCell(colNum).getStringCellValue().equalsIgnoreCase(testCaseName)) {
				for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++)
					testdata.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
				break;
			}
		}
		return testdata;
	}

	public String cellToString(XSSFCell cell) {
		int type = cell.getCellType();
		Object result;
		switch (type) {
		case XSSFCell.CELL_TYPE_NUMERIC: // 0
			result = cell.getNumericCellValue();
			break;
		case XSSFCell.CELL_TYPE_STRING: // 1
			result = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_FORMULA: // 2
			throw new RuntimeException("We can't evaluate formulas in Java");
		case XSSFCell.CELL_TYPE_BLANK: // 3
			result = "-";
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN: // 4
			result = cell.getBooleanCellValue();
			break;
		case XSSFCell.CELL_TYPE_ERROR: // 5
			throw new RuntimeException("This cell has an error");
		default:
			throw new RuntimeException("We don't support this cell type: " + type);
		}
		return result.toString();
	}

	public int getRowCount(String sheetName) {
		return workbook.getSheet(sheetName).getLastRowNum() + 1;
	}

	public int getFirstRowNum(String sheetName) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		return sheet.getFirstRowNum();
	}

	public int getLastRowNum(String sheetName) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		return sheet.getLastRowNum();
	}

	public boolean setCellData(String filePath, String sheetName, String colName, int rowNum, String data) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		FileOutputStream fileOut = null;
		try {
			if (rowNum < 0)
				return false;
			int colNum = -1;
			row = sheet.getRow(rowNum);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum = i;
			}
			if (colNum == -1)
				return false;
			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);
			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);
			cell.setCellValue(data);
			fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addSheet(String filePath, String sheetName) {
		FileOutputStream fileOut = null;
		try {
			workbook.createSheet(sheetName);
			fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeSheet(String filePath, String sheetName) {
		FileOutputStream fileOut = null;
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return false;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

//	public boolean addColumn(String filePath, String sheetName, String colName) {
//		FileOutputStream fileOut = null;
//		try {
//			fis = new FileInputStream(filePath);
//			XSSFWorkbook workbook = new XSSFWorkbook(fis);
//			int index = workbook.getSheetIndex(sheetName);
//			if (index == -1)
//				return false;
//			XSSFCellStyle style = workbook.createCellStyle();
//			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
//			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//			XSSFSheet sheet = workbook.getSheetAt(index);
//			XSSFRow row = sheet.getRow(0);
//			XSSFCell cell = null;
//			if (row == null)
//				row = sheet.createRow(0);
//			if (row.getLastCellNum() == -1)
//				cell = row.createCell(0);
//			else
//				cell = row.createCell(row.getLastCellNum());
//			cell.setCellValue(colName);
//			cell.setCellStyle(style);
//			fileOut = new FileOutputStream(filePath);
//			workbook.write(fileOut);
//			fileOut.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}

//	public boolean removeColumn(String filePath, String sheetName, int colNum) {
//		XSSFSheet sheet = workbook.getSheet(sheetName);
//		FileOutputStream fileOut = null;
//		try {
//			if (!isSheetExist(sheetName))
//				return false;
//			sheet = workbook.getSheet(sheetName);
//			XSSFCellStyle style = workbook.createCellStyle();
//			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
//			style.setFillPattern(XSSFCellStyle.NO_FILL);
//			for (int i = 0; i < getRowCount(sheetName); i++) {
//				row = sheet.getRow(i);
//				if (row != null) {
//					cell = row.getCell(colNum);
//					if (cell != null) {
//						cell.setCellStyle(style);
//						row.removeCell(cell);
//					}
//				}
//			}
//			fileOut = new FileOutputStream(filePath);
//			workbook.write(fileOut);
//			fileOut.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}

	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	public int getColumnCount(String sheetName, int rowNum) {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		if (row == null)
			return -1;
		return row.getLastCellNum();
	}

	public String convertXSSFCellToString(XSSFCell cell) {
		String cellValue = null;
		if (cell != null) {
			cellValue = cell.toString();
			cellValue = cellValue.trim();
		} else {
			cellValue = "";
		}
		return cellValue;
	}

	public HashMap<String, List<String>> getClassNameWithTestCase(String sheetName, String ClassNameHeader, String TestCaseNameHeader, String executionTypeHeader) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		int classNamecol = getColHeaderNum(sheetName, ClassNameHeader);
		int TestCaseNamecol = getColHeaderNum(sheetName, TestCaseNameHeader);
		int executionTypecol = getColHeaderNum(sheetName, executionTypeHeader);
		int rowCount = sheet.getLastRowNum();
		String testCaseName = null;
		String className = null;
		for (int i = 1; i <= rowCount; i++) {
			if (getCellData(sheetName, i, executionTypecol).equalsIgnoreCase("yes")) {
				testCaseName = getCellData(sheetName, i, TestCaseNamecol);
				className = getCellData(sheetName, i, classNamecol);
				if (!map.containsKey(className)) {
					List<String> list = new ArrayList<String>();
					list.add(testCaseName);
					map.put(className, list);
				} else {
					// map.put(map.get(className)., );
					map.get(className).add(testCaseName);
				}
			}
		}
		return map;
	}

	public HashMap<String, HashMap<String, String>> getExcelDataAll(String sheetName, String Flag, String FlagValue, String Key) throws Exception {
		XSSFSheet sheet = workbook.getSheet(sheetName);
		int flagIndex, keyIndex;
		flagIndex = getColNum(sheetName, Flag);
		keyIndex = getColNum(sheetName, Key);
		int lastRow = sheet.getLastRowNum();
		LinkedHashMap<String, HashMap<String, String>> result = new LinkedHashMap<String, HashMap<String, String>>(lastRow);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			if (getCellData(sheetName, i, flagIndex).equalsIgnoreCase(FlagValue)) {
				LinkedHashMap<String, String> testdata = new LinkedHashMap<String, String>();
				for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++) {
					try {
						sheet.getRow(0).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						testdata.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				try {
					result.put(sheet.getRow(i).getCell(keyIndex).getStringCellValue(), testdata);
				} catch (Throwable e) {
				}
			}
		}
		TestData = result;
		return result;
	}
	
	
	/*public static void main(String args[]) throws Exception{
		String fileName=Constants.datasheetPath+"Datasheet.xlsx";
		ExcelSheetReader excel = new ExcelSheetReader();
		String sheetName = "AdditionalDriver";
		System.out.println(excel.getCellData(sheetName, excel.getRowNum(sheetName, "AD 10"), excel.getColNum(sheetName, "First Name")));
		sheetName="AdditionalVehicle";
		System.out.println(excel.getCellData(sheetName, excel.getRowNum(sheetName, "AV 6"), excel.getColNum(sheetName, "Cost New")));

	}*/
}
