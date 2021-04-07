package cli.Reporting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import cli.Utilities.Constants;


public class ExtentManager {

	public static ExtentReports extent;
	public static ExtentHtmlReporter htmlReporter;
    public static ExtentTest test;
	static DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	static Date date = new Date();

	static String d=dateFormat.format(date);
	static String filename="Execution_Summary" +d+ ".html";


	public static ExtentReports getReporter() {
		if (extent == null) {

			htmlReporter = new ExtentHtmlReporter(Constants.reportPath+filename);

			//Set HTML reporting file location
			//String workingDir = System.getProperty("user.dir");
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);

		}
		return extent;
	}
}
