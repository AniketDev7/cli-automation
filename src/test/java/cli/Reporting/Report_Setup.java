package cli.Reporting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import cli.Utilities.Constants;
import cli.Utilities.Create_TestNGXML;
import cli.Utilities.Create_TestNGXML;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;


public class Report_Setup {

	public static ExtentReports extent;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentTest test;
	//public static ExtentTest parentTest;
	public static ExtentTest childTest;

	static DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	static Date date = new Date();

	static String d=dateFormat.format(date);
	static String filename="Execution_Summary_" +d+ ".html";

	public static ExtentReports getReporter() { 
		if (extent == null) {
			//Set HTML reporting file location
			htmlReporter = new ExtentHtmlReporter(Constants.reportPath+filename);
			htmlReporter.config().setTheme(Theme.DARK);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		} 
		
		return extent; 
	}

	public static void InitializeReport(String testCaseName, String Sheetname)
	{
		System.out.println(" Test Execution Report - > " + filename);

		//Report_Setup.getReporter();
		//parentTest = extent.createTest(Sheetname);
		//test = extent.createTest(testCaseName, "Execution Report of Test case-->  "+testCaseName);
		test = Create_TestNGXML.parentTest.createNode(testCaseName);
	}

}
