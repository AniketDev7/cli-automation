package cli.BusinessFlows;

import org.openqa.selenium.WebDriver;

import cli.Utilities.API_calls;
import cli.Utilities.Excel_Handling;
import cli.Utilities.WrapperMethods;
import cli.Plugins.Auth_script;
import cli.Plugins.Bulk_publish_script;
import cli.Plugins.Export_script;
import cli.Plugins.Import_script;
import cli.Plugins.Setup;
import cli.Plugins.SyncPipe;

public class CLI_v1 {

	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	static WrapperMethods method = new WrapperMethods();

	public CLI_v1(WebDriver d, String tcID) {
		this.driver = d;
		this.TC_ID = tcID;
	}

	public static void Execute(String TC_ID, WebDriver driver) throws Throwable
	{		

		try{	
			
			System.out.println("Current executing test case is -> "+ Excel_Handling.Get_Data(TC_ID,"tc_id"));
			
			//Setting up the Git repo and Env region
			Setup.Execute(TC_ID, driver);
			
			if (Excel_Handling.Get_Data(TC_ID,"Plugin").contains("Auth")) 
			{	
				Auth_script.Execute(TC_ID, driver);
			}
			
			if (Excel_Handling.Get_Data(TC_ID,"Plugin").contains("Export")) 
			{	
			
				Export_script.Execute(TC_ID, driver);
			}
			if (Excel_Handling.Get_Data(TC_ID,"Plugin").contains("Import")) 
			{
				
				Import_script.Execute(TC_ID, driver);
			}
			
			if (Excel_Handling.Get_Data(TC_ID,"Plugin").contains("Bulk-publish")) 
			{
				
				Bulk_publish_script.Execute(TC_ID, driver);
			}
			
		}catch(Exception e){

			e.printStackTrace();
		}



	}

}
