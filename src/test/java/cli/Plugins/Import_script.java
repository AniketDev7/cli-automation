package cli.Plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import cli.Reporting.Extent_Reporting;
import cli.Utilities.API_calls;
import cli.Utilities.Excel_Handling;
import cli.Utilities.WrapperMethods;
import cli.Plugins.SyncPipe;


public class Import_script {


	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	static WrapperMethods method = new WrapperMethods();

	public Import_script(WebDriver d, String tcID) {
		this.driver = d;
		this.TC_ID = tcID;
	}
	

	public static void Execute(String TC_ID, WebDriver driver) throws Throwable
	{		

		try{	

			System.out.println("Executing Import script now...");

			String[] command ={ "cmd",};
			Process p;

			try {

				String locale = Excel_Handling.Get_Data(TC_ID,"locale");
				String target_stack = Excel_Handling.Get_Data(TC_ID,"target_stack");
				String Mg_token = Excel_Handling.Get_Data(TC_ID,"management_token");
				String currentDir = System.getProperty("user.dir");
				String path = currentDir+"\\Exported_content";
				String alias = Excel_Handling.Get_Data(TC_ID,"alias");
				String conf_path =  currentDir+"\\cli\\packages\\contentstack-import\\example_config\\index.json";
				String md = Excel_Handling.Get_Data(TC_ID, "module");
				
				//Creating a New stack
				API_calls.Create_stack(TC_ID, driver);
				String key = API_calls.API_key;
				
				if (Excel_Handling.Get_Data(TC_ID,"token_typ").contains("Auth"))
				{
					method.auth(driver, "csdx login -u ", "aniket.shikhare@contentstack.com", "Aniket@1511", "Contentstack account authenticated successfully!", " Auth token added");
					
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					if (Excel_Handling.Get_Data(TC_ID, "module").contains("NA")) 
					{
						if (Excel_Handling.Get_Data(TC_ID, "config_file").contains("Yes")) {
							stdin.println("csdx cm:import -A " + "-c " + conf_path);
						} else {
							stdin.println("csdx cm:import -A " + " -l " + locale + " -s " + key + " -d " + path);
						} 
					} else {
						stdin.println("csdx cm:import -A " + " -l " + locale + " -s " + key + " -d " + path+ " -m "+md );
					}
					stdin.close();
					p.waitFor();
					String log_path = path + "\\logs\\import\\success.log";
					BufferedReader br = new BufferedReader(new FileReader(log_path));
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null) {
						sb.append(line).append("\n");
						line = br.readLine();
					}
					String FL = sb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (FL.contains("has been imported")) {

						System.out.println("<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed..",	" The exported stack data is imported in new stack >> " , driver);
					} else {
						System.out.println("---------- Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", "Test case failed & the log is >>>> "+ FL, driver);
					} 
				}

				if (Excel_Handling.Get_Data(TC_ID,"token_typ").contains("Management")) 
				{
					API_calls.Create_stack(TC_ID, driver);
					//API_call
					String M_token = API_calls.M_Token;
					method.Mg_token(driver, "csdx auth:tokens:add -m ", alias, key, M_token, "token added successfully!", "Management token added successfully >>");

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					if (Excel_Handling.Get_Data(TC_ID, "module").contains("NA")) 
					{						
						if (Excel_Handling.Get_Data(TC_ID, "config_file").contains("Yes")) {
							stdin.println("csdx cm:import -a " + alias + " -c " + conf_path);
						} else {
							stdin.println("csdx cm:import -a " + alias + " -l " + locale + " -d " + path);
						} 
					}else {
						stdin.println("csdx cm:import -a " + alias + " -l " + locale + " -d " + path+ " -m "+ md);
					}
					stdin.close();
					p.waitFor();
					String log_path = path + "\\logs\\import\\success.log";
					BufferedReader br = new BufferedReader(new FileReader(log_path));
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null) {
						sb.append(line).append("\n");
						line = br.readLine();
					}
					String fileAsString = sb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsString.contains("has been imported")) {

						System.out.println("Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the Exported data is >> " + fileAsString, driver);

					} else {
						System.out.println("Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", "  <<< The stack importing using Management token failed... ", driver);

					} 
				} 


			} catch (Exception e) {
				e.printStackTrace();
			}




			class SyncPipe implements Runnable
			{
				public SyncPipe(InputStream istrm, OutputStream ostrm) {
					istrm_ = istrm;
					ostrm_ = ostrm;
				}
				public void run() {
					try
					{
						final byte[] buffer = new byte[1024];
						for (int length = 0; (length = istrm_.read(buffer)) != -1; )
						{
							ostrm_.write(buffer, 0, length);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				OutputStream ostrm_;
				InputStream istrm_;
			}

		}catch(Exception e){

			e.printStackTrace();
		}

	}

}
