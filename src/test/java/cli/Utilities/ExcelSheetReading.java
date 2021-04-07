package cli.Utilities;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelSheetReading {

	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static XSSFSheet sheet1;
	public static XSSFCell cell;
	public static XSSFRow row;
	public static FileInputStream fis = null;
	public static FileOutputStream fileOut = null;
	@SuppressWarnings("rawtypes")
	//public static HashMap <String, HashMap> TestData;
	public static HashMap <String, HashMap> TestAdditionalDriver;
	public static HashMap <String, HashMap> TestAdditionalVehicle;
	
	
	public static String fileFullPath;
	public static String srcSheetName;
	public static String resultPath="";
	public static String sheetName="";

	@SuppressWarnings("static-access")
	public void ExcelReader(String sheetname) {
		String fileName=Constants.datasheetPath+"Datasheet.xlsx"; 
		//String fileName="C:\\a\\Datasheet.xlsx";
		
		try {
			fis = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetname);
			//srcSheetName=sheetname;
			fileFullPath=fileName;	
			this.sheetName=sheetname;
			fis.close();		
			
		} catch (FileNotFoundException fnfEx) {
			fnfEx.printStackTrace();
			System.out.println(fileName + " is not Found. please check the file name.");
			System.exit(0);
		} catch (IOException ioEx) {
			System.out.println(fis + " is not Found. please check the path.");
		} catch (Exception ex) {
			System.out.println("There is error reading/loading xls file, due to " + ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public HashMap<String, HashMap> getExcelDataSheet() throws Exception {
		sheet = workbook.getSheet(sheetName);
		int keyIndex = 0;
		if(sheetName.equalsIgnoreCase("AdditionalDriver"))
		keyIndex=findColumnIndex("Driver_reference_number",sheet);
		else if(sheetName.equalsIgnoreCase("AdditionalVehicle"))
			keyIndex=findColumnIndex("Vehicle_reference_number",sheet);
		
		//keyIndex=findColumnIndex(Key);
		
		
		int lastRow = sheet.getLastRowNum();
	
		
		LinkedHashMap<String, HashMap> result = new LinkedHashMap<String, HashMap>(lastRow);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			
			
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
		if(sheetName.equalsIgnoreCase("AdditionalDriver"))
			TestAdditionalDriver=result;
			else if(sheetName.equalsIgnoreCase("AdditionalVehicle"))
				TestAdditionalVehicle=result;
		
		
		return result;
	}

	public int findColumnIndex(String ColumnHeader,XSSFSheet sheet)
	{  
		int ColumnCount,CurrentColumn;
		CurrentColumn=-1;
		
		XSSFRow fr=sheet.getRow(0);
		  ColumnCount=fr.getLastCellNum()-fr.getFirstCellNum();
		
	
		
		  for (int i=0;i<ColumnCount-1;i++)
		  {
			  if(fr.getCell(i).getStringCellValue().contains(ColumnHeader)) 
			  {
				  
				  CurrentColumn=i;
				
				break;		
			  }
		  }
		  
		  return CurrentColumn;
	}	
	
	@SuppressWarnings("unchecked")
	public static String Get_Data(String ReferenceNum,String ColumnHeader,String SheetName)
	{  
		LinkedHashMap<String,String> TC=null;
		
		if(SheetName.equalsIgnoreCase("AdditionalDriver"))	
			
			TC=(LinkedHashMap<String,String>) TestAdditionalDriver.get(ReferenceNum);
		
		else if(SheetName.equalsIgnoreCase("AdditionalVehicle"))
			
				TC=(LinkedHashMap<String,String>) TestAdditionalVehicle.get(ReferenceNum);
		try
		{
		
		return TC.get(ColumnHeader) ;
		}
		catch(Throwable e)
		{
			return "null";
		}
		
	}	
	
	//********************************************************************************
	
	@SuppressWarnings("unchecked")
	public static String Get_Data1(String ReferenceNum,String ColumnHeader,String SheetName)
	{  
		LinkedHashMap<String,String> TC=null;
		TC=(LinkedHashMap<String,String>) TestAdditionalVehicle.get(ReferenceNum);
		try
		{
		
		return TC.get(ColumnHeader) ;
		}
		catch(Throwable e)
		{
			return "null";
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap<String, HashMap> getExcelDataSheet1() throws Exception {
		sheet1 = workbook.getSheet("AdditionalVehicle");
		int keyIndex = 0;			
		keyIndex=findColumnIndex("Vehicle_reference_number",sheet1);		
		int lastRow = sheet1.getLastRowNum();
	
		
		LinkedHashMap<String, HashMap> result = new LinkedHashMap<String, HashMap>(lastRow);
		for (int i = 1; i <= sheet1.getLastRowNum(); i++) {
			
			
				LinkedHashMap<String, String> testdata = new LinkedHashMap<String, String>();
				for (int j = 0; j < sheet1.getRow(i).getLastCellNum(); j++)
				{	
					try
					{
						
						sheet1.getRow(0).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						sheet1.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					testdata.put(sheet1.getRow(0).getCell(j).getStringCellValue(),sheet1.getRow(i).getCell(j).getStringCellValue() );
				 //  System.out.println(sheet1.getRow(0).getCell(j).getStringCellValue()+" " +sheet1.getRow(i).getCell(j).getStringCellValue());
					}
					catch(Throwable e)
					{
						//System.out.println(sheet1.getRow(0).getCell(j).getRichStringCellValue()+" "+sheet1.getRow(i).getCell(j).getCellType());
						// System.out.println(sheet1.getRow(0).getCell(j).getStringCellValue()+" " +sheet1.getRow(i).getCell(j).getStringCellValue());
						//e.printStackTrace();
					}
				}	
				try
				{
					result.put(sheet1.getRow(i).getCell(keyIndex).getStringCellValue(),testdata);
					
				}
				catch(Throwable e)
				{
					
				}
				
			
		}
		 TestAdditionalVehicle=result;
		
		
		return result;
	}
	
	public static void main(String args[]) throws Exception{
		ExcelSheetReading el = new ExcelSheetReading();
		el.ExcelReader("AdditionalVehicle");
		el.getExcelDataSheet1();		
		System.out.println(el.Get_Data("AV 1", "VIN", "AdditionalVehicle"));
		ExcelSheetReading el1 = new ExcelSheetReading();
		el1.ExcelReader("AdditionalDriver");
		el1.getExcelDataSheet();		
		System.out.println(el.Get_Data("AD 1", "First Name", "AdditionalDriver"));
		
		
	}
	
}
