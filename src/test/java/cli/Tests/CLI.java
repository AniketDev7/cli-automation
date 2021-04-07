package cli.Tests;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import cli.BusinessFlows.CLI_v1;
//import com.Menu_Pages.BSC_Backend_Login;
import cli.Reporting.Extent_Reporting;
import cli.Utilities.Driver_Setup;



public class CLI extends Driver_Setup{

	public WebDriver driver;


	@BeforeClass
	public void setUp() 
	{
		driver = getDriver();
	}	

	@Test
	public void TC_001() throws Throwable 
	{
		System.out.println("CLI Test Automation suite started --> ");
		     
       
		CLI_v1.Execute(TC_ID, driver);
	}



}
