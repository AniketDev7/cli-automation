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


public class Export_script {


	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	//WrapperMethods method = new WrapperMethods();
	static WrapperMethods method = new WrapperMethods();

	public Export_script(WebDriver d, String tcID) {
		this.driver = d;
		this.TC_ID = tcID;
	}



	public static void Execute(String TC_ID, WebDriver driver) throws Throwable
	{		

		try{	

			String[] command ={ "cmd",};
			Process p;
			Process q;

			try {

				String locale = Excel_Handling.Get_Data(TC_ID,"locale");
				String source_stack = Excel_Handling.Get_Data(TC_ID,"source_stack");
				String currentDir = System.getProperty("user.dir");
				String path = currentDir+"\\Exported_content\\"+ Excel_Handling.Get_Data(TC_ID,"data_path");
				String alias = Excel_Handling.Get_Data(TC_ID,"alias");
				String source_key = Excel_Handling.Get_Data(TC_ID, "source_stack");
				String md = Excel_Handling.Get_Data(TC_ID, "module");

				String conf_path =  currentDir+"\\cli\\packages\\contentstack-export\\example_config\\index.json";
				//String target_key = Excel_Handling.Get_Data(TC_ID, "target_stack");


				if (Excel_Handling.Get_Data(TC_ID,"token_typ").contains("Auth"))
				{
					method.auth(driver, "csdx login -u ", "aniket.shikhare@contentstack.com", "Aniket@7890", "Contentstack account authenticated successfully!", " Auth token added");

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					if (Excel_Handling.Get_Data(TC_ID, "module").contains("NA")) 
					{
						if (Excel_Handling.Get_Data(TC_ID, "config_file").contains("Yes")) {
							stdin.println("csdx cm:export -A " + "-c " + conf_path);
						} else {
							stdin.println("csdx cm:export -A " + " -l " + locale + " -s " + source_stack + " -d " + path);
						} 
					} else {
						stdin.println("csdx cm:export -A " + " -l " + locale + " -s " + source_stack + " -d " + path+ " -m "+md );
					}
					stdin.close();
					p.waitFor();
					String log_path = path + "\\logs\\export\\success.log";
					BufferedReader br = new BufferedReader(new FileReader(log_path));
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null) {
						sb.append(line).append("\n");
						line = br.readLine();
					}
					String fileAsString = sb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsString.contains("exported")) {

						System.out.println("<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed..",	"<<< The stack has been exported successfully using Auth token >>> " , driver);
					} else {
						System.out.println("--------- Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", "  <<< The stack exporting using Auth token is failed...->  " +fileAsString, driver);
					} 
				}

				if (Excel_Handling.Get_Data(TC_ID,"token_typ").contains("Management")) 
				{
					API_calls.Create_Management_token(TC_ID, driver,source_key);
					String Mg_token = API_calls.M_Token;
					method.Mg_token(driver, "csdx auth:tokens:add -m ", alias, source_key, Mg_token, "token added successfully!", "Management token added successfully >>");

					q = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(q.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(q.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(q.getOutputStream());
					if (Excel_Handling.Get_Data(TC_ID, "module").contains("NA")) 
					{						
						if (Excel_Handling.Get_Data(TC_ID, "config_file").contains("Yes")) {
							stdin.println("csdx cm:export -a " + alias + " -c " + conf_path);
						} else {
							stdin.println("csdx cm:export -a " + alias + " -l " + locale + " -d " + path);
						} 
					}else {
						stdin.println("csdx cm:export -a " + alias + " -l " + locale + " -d " + path+ " -m "+ md);
					}
					stdin.close();
					q.waitFor();
					String log_path = path + "\\logs\\export\\success.log";
					BufferedReader br = new BufferedReader(new FileReader(log_path));
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null) {
						sb.append(line).append("\n");
						line = br.readLine();
					}
					String fileAsString = sb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsString.contains("exported")) {

						System.out.println("<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed..", " <<< The stack has been exported successfully using Management token : " + alias+ " >>>  " , driver);

					} else {
						System.out.println("----------- Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", "  <<< The stack exporting using Management token is failed & logs are-> "+fileAsString, driver);

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
