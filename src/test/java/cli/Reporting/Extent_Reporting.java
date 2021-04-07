package cli.Reporting;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import cli.Utilities.Common_Functions;

public class Extent_Reporting {	
	
		public static void Log_Pass(String stepName, String passMessage, WebDriver driver){
		
		Report_Setup.test.log(Status.PASS, passMessage);
       
	}
	
	public static void Log_Fail(String stepName, String failMessage, WebDriver driver) throws InterruptedException, IOException{

		
		Report_Setup.test.log(Status.FAIL, failMessage, MediaEntityBuilder.createScreenCaptureFromPath(Common_Functions.captureScreenshot(driver)).build());
		Thread.sleep(1000);
		//String img = Report_Setup.test.addScreenCapture(Common_Functions.captureScreenshot(driver));
		//Report_Setup.test.log(Status.FAIL, stepName);
	}
	
	public static void Log_Pass_with_Screenshot(String stepName, String passMessage, WebDriver driver) throws InterruptedException, IOException{
		
		/*
		 * Report_Setup.test.log(Status.PASS, stepName, passMessage);
		 * Thread.sleep(1000); //String img =
		 * Report_Setup.test.addScreenCapture(Common_Functions.captureScreenshot(driver)
		 * ); Report_Setup.test.log(Status.PASS, stepName);
		 * 
		 */
		Report_Setup.test.log(Status.PASS, passMessage, MediaEntityBuilder.createScreenCaptureFromPath(Common_Functions.captureScreenshot(driver)).build());
		Thread.sleep(1000);
		//String img = Report_Setup.test.addScreenCapture(Common_Functions.captureScreenshot(driver));
		//Report_Setup.test.log(Status.PASS, stepName);
	}
	

}
