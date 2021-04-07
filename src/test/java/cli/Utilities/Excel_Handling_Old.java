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
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel_Handling_Old {

	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static XSSFCell cell;
	public static XSSFRow row;
	public static FileInputStream fis = null;
	public static FileOutputStream fileOut = null;
	@SuppressWarnings("rawtypes")
	public static HashMap <String, HashMap> TestData;
	public static String fileFullPath;
	public static String srcSheetName;
	public static String resultPath="";
	public static String resultSheetName="";

	public void ExcelReader(String fileName, String sheetname,String ResultPath,String ResultName) {
		try {
			fis = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetname);
			srcSheetName=sheetname;
			fileFullPath=fileName;
			resultPath=ResultPath;
			resultSheetName=ResultName;
//			createcopy();
			fis.close();
			fis = new FileInputStream(new File(resultPath));
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(resultSheetName);
			fileFullPath=resultPath;
			srcSheetName=resultSheetName;
			
			
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
		try {
			int rowCount = sheet.getLastRowNum();
			System.out.println("rowCount " + rowCount);
			for (int i = 0; i <= rowCount; i++) {
				if (getCellData(i, colNum).equalsIgnoreCase(value)) {
					return i;
				}
			}
			return -1;
		} catch (Exception e) {
			throw (e);
		}
	}

	public String[] getRowData(int rowNum) throws Exception {
		String[] temp = new String[sheet.getRow(rowNum).getLastCellNum()];
		for (int i = 0; i < temp.length; i++)
			temp[i] = getCellData(rowNum, i);
		return temp;
	}

	public String getCellData(int rowNum, int colNum) throws Exception {
		try {
			cell = sheet.getRow(rowNum).getCell(colNum);
			String cellData = cell.getStringCellValue();
			return cellData;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getCellIntData(int rowNum, int colNum) throws Exception {
		try {
			cell = sheet.getRow(rowNum).getCell(colNum);
			String CellData = String.valueOf(cell.getNumericCellValue());
			CellData = CellData.substring(0, CellData.indexOf("."));
			return CellData;
		} catch (Exception e) {
			return "";
		}

	}

	@SuppressWarnings("deprecation")
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";
			int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);
			if (cell == null)
				return "";
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC || cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	@SuppressWarnings("deprecation")
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC || cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	public List<HashMap<String, String>> getExcelData() {
		int lastRow = sheet.getLastRowNum();
		System.out.println(lastRow);
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>(lastRow);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			HashMap<String, String> testdata = new HashMap<String, String>();
			for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++)
			{	
				try
				{
			     System.out.println("i:"+i+" "+"j:"+j);
				testdata.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
			    
				}
				catch(Throwable e)
				{
					//e.printStackTrace();
				}
			}	
			result.add(testdata);
		}
		return result;
	}
	
	public HashMap<String, String> getExcelRowData(int rowNum) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (int j = 0; j < sheet.getRow(rowNum).getLastCellNum(); j++)
			map.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(rowNum).getCell(j).getStringCellValue());
		return map;
	}

	@SuppressWarnings("deprecation")
	public static String cellToString(HSSFCell cell) {
		
		int type = cell.getCellType();
		Object result;
		switch (type) {
		case HSSFCell.CELL_TYPE_NUMERIC: // 0
			result = cell.getNumericCellValue();
			break;
		case HSSFCell.CELL_TYPE_STRING: // 1
			result = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 2
			throw new RuntimeException("We can't evaluate formulas in Java");
		case HSSFCell.CELL_TYPE_BLANK: // 3
			result = "-";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // 4
			result = cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 5
			throw new RuntimeException("This cell has an error");
		default:
			throw new RuntimeException("We don't support this cell type: " + type);
		}
		return result.toString();
	}

	public int getRowCount(String sheetName) {
		return workbook.getSheet(sheetName).getLastRowNum() + 1;
	}

	public static int getFirstRowNum() {
		return sheet.getFirstRowNum();
	}

	public static int getLastRowNum() {
		return sheet.getLastRowNum();
	}

	public boolean setCellData(String filepath, String sheetName, String colName, int rowNum, String data) {
		try {
			if (rowNum <= 0)
				return false;
			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
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
			fileOut = new FileOutputStream(filepath);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addSheet(String filePath, String sheetName) {
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
//		try {
//			fis = new FileInputStream(filePath);
//			XSSFWorkbook workbook = new XSSFWorkbook(fis);
//			int index = workbook.getSheetIndex(sheetName);
//			if (index == -1)
//				return false;
//			XSSFCellStyle style = workbook.createCellStyle();
//			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
//			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
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
//		try {
//			if (!isSheetExist(sheetName))
//				return false;
//			sheet = workbook.getSheet(sheetName);
//			XSSFCellStyle style = workbook.createCellStyle();
//			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
//			style.setFillPattern(HSSFCellStyle.NO_FILL);
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

	public int getColumnCount(String sheetName) {
		if (!isSheetExist(sheetName))
			return -1;
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);
		if (row == null)
			return -1;
		return row.getLastCellNum();
	}

	public static String HSSFCellToString(HSSFCell cell) {
		String cellValue = null;
		if (cell != null) {
			cellValue = cell.toString();
			cellValue = cellValue.trim();
		} else {
			cellValue = "";
		}
		return cellValue;
	}
	
/*	
	
	public HashMap<String, HashMap> getExcelDataAll() {
		int lastRow = sheet.getLastRowNum();
		//System.out.println(lastRow);
		HashMap<String, HashMap> result = new HashMap<String, HashMap>(lastRow);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			HashMap<String, String> testdata = new HashMap<String, String>();
			for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++)
			{	
				try
				{
			 //    System.out.println("i:"+i+" "+"j:"+j);
				testdata.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
			    
				}
				catch(Throwable e)
				{
					//e.printStackTrace();
				}
			}	
			result.put(sheet.getRow(i).getCell(0).getStringCellValue(),testdata);
		}
		TestData=result;
		return result;
	}
	
	*/
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public HashMap<String, HashMap> getExcelDataAll(String sheetName,String Flag,String FlagValue,String Key) throws Exception {
		sheet = workbook.getSheet(sheetName);
		int flagIndex,keyIndex;
		flagIndex=findColumnIndex(Flag);
		keyIndex=findColumnIndex(Key);
		
		
		int lastRow = sheet.getLastRowNum();
		//System.out.println(lastRow);
		
		LinkedHashMap<String, HashMap> result = new LinkedHashMap<String, HashMap>(lastRow);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			
			if (getCellData(i,flagIndex).equalsIgnoreCase(FlagValue))
			{
				LinkedHashMap<String, String> testdata = new LinkedHashMap<String, String>();
				for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++)
				{	
					try
					{
						
						sheet.getRow(0).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					testdata.put(sheet.getRow(0).getCell(j).getStringCellValue(),sheet.getRow(i).getCell(j).getStringCellValue() );
				 //  System.out.println(sheet.getRow(0).getCell(j).getStringCellValue()+" " +sheet.getRow(i).getCell(j).getStringCellValue());
					}
					catch(Throwable e)
					{
						//System.out.println(sheet.getRow(0).getCell(j).getRichStringCellValue()+" "+sheet.getRow(i).getCell(j).getCellType());
						// System.out.println(sheet.getRow(0).getCell(j).getStringCellValue()+" " +sheet.getRow(i).getCell(j).getStringCellValue());
						//e.printStackTrace();
					}
				}	
				try
				{
					result.put(sheet.getRow(i).getCell(keyIndex).getStringCellValue(),testdata);
					
				}
				catch(Throwable e)
				{
					
				}
				
			}
		}
		TestData=result;
		return result;
	}
	public int findColumnIndex(String ColumnHeader)
	{  int ColumnCount,CurrentColumn;
	CurrentColumn=-1;
		XSSFRow fr=sheet.getRow(0);
		  ColumnCount=fr.getLastCellNum()-fr.getFirstCellNum();
		
	
		
		  for (int i=0;i<=ColumnCount-1;i++)
		  {
			  if(fr.getCell(i).getStringCellValue().contains(ColumnHeader)) 
			  {
				  
				  CurrentColumn=i;
				
				break;		
			  }
		  }
		  
		  return CurrentColumn;
	}	
	public static String Get_Data(String TestCase,String ColumnHeader)
	{  
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,String> TC=(LinkedHashMap<String,String>) TestData.get(TestCase);
		
		try
		{
		
		return TC.get(ColumnHeader) ;
		}
		catch(Throwable e)
		{
			return "null";
		}
		
	}	
	 
	public String Put_Data(String TestCase,String ColumnHeader,String Value)
	{  
	try
	{
		String data="";
		//@SuppressWarnings("unchecked")
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,String> TC=(LinkedHashMap<String,String>) TestData.get(TestCase);
		
		
		if (TC==null)
			return "Fail";
		
		if (TC.containsKey(ColumnHeader))
		{
			 data=TC.get(ColumnHeader);
			data=data+";"+Value;
		}
		else
		{
			data=Value;
		
		}
		
		 TC.put(ColumnHeader,data) ;
		 return "success";
	}
		 catch(Throwable t)
		 {
			 t.printStackTrace();
		    return "fail";
		 }
	}	
	
	public String Put_Data_Replace(String TestCase,String ColumnHeader,String Value)
	{  
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,String> TC=(LinkedHashMap<String,String>) TestData.get(TestCase);
		
		
		@SuppressWarnings("unused")
		String data=TC.get(ColumnHeader);
		
		
		
			
		
		 TC.put(ColumnHeader,Value) ;
		 return "success";
	}
	
	public boolean setCellDataWithCondtion(String colName, String rowName,String rowValue, String data) {
		try {
			
			
		//	int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			int rowNameNum=-1;
			
		//	if (index == -1)
		//		return false;
		//	sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
		//	System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+row.getLastCellNum());
			for (int i = 0; i < row.getLastCellNum(); i++) {
				String str="";
				try
				{
					 str=row.getCell(i).getStringCellValue().trim();
					
				}
				catch(Throwable T)
				{
					
				}
				if (str.equals(colName))
				{
					colNum = i;
			//		System.out.println("+++++++++++++++++++++++++++++++++"+colNum);
					break;
				}
					
			}
			if (colNum == -1)
			{
				
				colNum=row.getLastCellNum();
				
				row.createCell(colNum);
				cell =row.getCell(colNum);
				cell.setCellValue(colName);
			 
			}
			for (int i = 0; i < row.getLastCellNum()-1; i++) {
				System.out.println(i+rowName+row.getCell(i).getStringCellValue());
				String s="";
				try
				{
					 s=row.getCell(i).getStringCellValue().trim();
					
				}
				catch(Throwable T)
				{
					
				}
				
				if (s.equals(rowName.trim()))
				{
					rowNameNum = i;
					break;
				}
				
			}
			if (rowNameNum == -1)
				return false;
		//	System.out.println("+++++++++++++++++++++++++++++++++RownameNumber"+rowNameNum);
			
			int rowNum=searchField(srcSheetName, rowNameNum, rowValue);
			//System.out.println(rowNum);
		//	System.out.println("+++++++++++++++++++++++++++++++++RowNumber"+rowNum);
			if (rowNum <= 0)
				return false;
			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum );
			if (row == null)
				row = sheet.createRow(rowNum );
			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);
		//	System.out.println("+++++++++++++++++++++++++++++++++RowNumber"+rowNum+"colNum"+colNum);
			cell.setCellValue(data);
			//System.out.println("+++++++++++++++++++++++++++++++++RowNumber"+rowNum+"colNum"+colNum+"data"+data);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean Write_Data(String rowName,String ColumnHeader)
	
	{  String s="";
		try {
			@SuppressWarnings("rawtypes")
			Map<String, HashMap> map = TestData;
			@SuppressWarnings("unused")
			HashMap<String,String> m=new HashMap<String,String>();
			for (String key: map.keySet()) {

			  //  System.out.println("key : " + key);
			  m=map.get(key);
			 s= Get_Data(key,ColumnHeader);
			 System.out.println(ColumnHeader+rowName+key+s);
			 if (s!=null)
			 setCellDataWithCondtion(  ColumnHeader, rowName,key, s);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return true;
		
	}	
	 
	public void close()
	{
		try
		{
			System.out.println(fileFullPath+srcSheetName);
		fileOut = new FileOutputStream(fileFullPath);
		
		workbook.write(fileOut);
		fileOut.close();
		
		}
		catch(Throwable t)
		{
			
		 t.printStackTrace();
		}
	}
	
	
	public boolean Write_Data_All(String rowName,String ColumnHeaders)
	
	{ 
		
		String ch[]=ColumnHeaders.split(";");
		
		for(String s:ch)
		{ //  System.out.println(s+"*********************************************"); 
			Write_Data(rowName,s);
		}
		
		close();
		return true;
		
	}	
	
		
	
}
