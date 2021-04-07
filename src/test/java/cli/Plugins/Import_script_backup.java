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
import cli.Utilities.Excel_Handling;
import cli.Utilities.WrapperMethods;
import cli.Plugins.SyncPipe;


public class Import_script_backup {


	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	WrapperMethods method = new WrapperMethods();


	public Import_script_backup(WebDriver d, String tcID) {
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
				String path = Excel_Handling.Get_Data(TC_ID,"data_path");
				String alias = Excel_Handling.Get_Data(TC_ID,"alias");


				if (Excel_Handling.Get_Data(TC_ID,"token_typ").contains("Auth"))
				{
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					//stdin.println("cd C:\\Users\\Aniket Shikhare\\Documents\\CLI\\CLI_optimized\\cli");
					stdin.println("csdx");
					stdin.println("csdx cm:import -A " + " -l " + locale + " -s " + target_stack + " -d " + path);
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
					System.out.println("Text from log ->>" + fileAsString);
					if (fileAsString.contains("has been imported succesfully!")) {

						System.out.println("Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed..",	" Test case is passed & the Exported data is >> " + fileAsString, driver);
					} else {
						System.out.println("Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " ", driver);
					} 
				}

				if (Excel_Handling.Get_Data(TC_ID,"token_typ").contains("Management")) 
				{
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					//stdin.println("cd C:\\Users\\Aniket Shikhare\\Documents\\CLI\\CLI_optimized\\cli");
					stdin.println("csdx cm:export -a "+ alias + " -l " + locale + " -d " + path);
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
					if (fileAsString.contains("has been exported succesfully!")) {

						System.out.println("Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the Exported data is >> " + fileAsString, driver);

					} else {
						System.out.println("Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " ", driver);

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
