package cli.Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import cli.Reporting.Report_Setup;

public class Create_TestNGXML {

	public static String sheet;
	public static ExtentTest parentTest;
	//public static ExtentReports extent;

	public List<XmlInclude> constructIncludes (String... methodNames) {
		List<XmlInclude> includes = new ArrayList<XmlInclude> ();
		for (String eachMethod : methodNames) {
			includes.add (new XmlInclude (eachMethod));
		}
		return includes;
	}

	@SuppressWarnings("deprecation")
	@Test     
	public void createXMLfile() throws IOException, InterruptedException {

		//calling out the excel datasheet instance to get all the "Y" data for setting up the testngxml
		Excel_Handling excel_Main = new Excel_Handling();

		excel_Main.copy(Constants.datasheetPath+"Datasheet.xlsx", Constants.datasheetPath+"Datasheet_Result.xlsx");

		Excel_Handling excel_Control = new Excel_Handling();
		Excel_Handling excel_Sheets = new Excel_Handling();

		excel_Control.ExcelReader(Constants.datasheetPath+"Datasheet.xlsx", "Control", Constants.datasheetPath+"Datasheet_Result.xlsx", "Control");
		try {

			excel_Control.getExcelDataAll("Control", "Execute", "Yes", "Sr_No");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		@SuppressWarnings({ "static-access", "rawtypes" })
		Map<String, HashMap> map_control = excel_Control.TestData;

		for(String key_control: map_control.keySet()){

			try {

				String TC_Name = map_control.get(key_control).toString();

				String[] TC_Name_New = TC_Name.split("TC_Name=");

				String[] TC_Name_New1 = TC_Name_New[1].split(",");

				//code added for separate sheets
				excel_Sheets.ExcelReader(Constants.datasheetPath+"Datasheet.xlsx", TC_Name_New1[0].trim(), Constants.datasheetPath+"Datasheet_Result.xlsx", TC_Name_New1[0].trim());

				excel_Sheets.getExcelDataAll(TC_Name_New1[0].trim(), "flag", "Yes", "tc_id");

				//Start - Code Added to get the current Sheet Name to pass that as Parent in Extent Report
				String [] S = TC_Name_New1[0].split(","); 
				sheet = TC_Name_New1[0];
				//End - Code Added to get the current Sheet Name to pass that as Parent in Extent Report

				Report_Setup.getReporter();
				parentTest = Report_Setup.extent.createTest(sheet);




			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			@SuppressWarnings({ "static-access", "rawtypes" })
			Map<String, HashMap> map_sheets = excel_Sheets.TestData;

			for(String key: map_sheets.keySet()){


				//creation of the testng xml based on parameters/data
				TestNG testNG = new TestNG();

				XmlSuite suite = new XmlSuite();
				suite.setName (new Common_Functions().GetXMLTagValue(Constants.configPath+"Config.xml", "Regression_Suite_Name"));

				suite.setParallel("tests");
				suite.setThreadCount(Integer.parseInt("1")); //Browser Instance count set to 1

				XmlTest test = new XmlTest(suite);

				test.setName(key);
				test.setPreserveOrder("true");
				test.addParameter("tcID", key);
				test.addParameter("browserType", "CHROME"); //Browser type hardcoded to CHROME
				test.addParameter("appURL", new Common_Functions().GetXMLTagValue(Constants.configPath+"Config.xml", "AppUrl")); 	        

				XmlClass testClass = new XmlClass();
				testClass.setName ("cli.Tests."+Excel_Handling.Get_Data(key, "class_name"));
				test.setXmlClasses (Arrays.asList (new XmlClass[] { testClass}));



				List<String> suites = new ArrayList<String>();
				final File f1 = new File(Create_TestNGXML.class.getProtectionDomain().getCodeSource().getLocation().getPath());

				File f = new File(f1+"\\testNG.xml");
				f.createNewFile();

				FileWriter fw = new FileWriter(f.getAbsoluteFile());

				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(suite.toXml());

				bw.close();

				suites.add(f.getPath());

				testNG.setTestSuites(suites);

				Report_Setup.InitializeReport(key,sheet);
				testNG.run();
				//Report_Setup.extent.removeTest(Report_Setup.test);
				Report_Setup.extent.flush();
				f.delete();
			}


		}


		Report_Setup.extent.flush();

	}


}
