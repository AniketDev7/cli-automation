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


public class Bulk_publish_script {


	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	static WrapperMethods method = new WrapperMethods();
	public static String M_Token;
	

	public Bulk_publish_script(WebDriver d, String tcID) {
		this.driver = d;
		this.TC_ID = tcID;
	}


	public static void Execute(String TC_ID, WebDriver driver) throws Throwable
	{		

		try{	
			System.out.println("Executing Bulk Publish script now...");

			String[] command ={ "cmd",};
			Process p;

			try {

				String locale = Excel_Handling.Get_Data(TC_ID,"locale").replace("|", "");
				String source_stack = Excel_Handling.Get_Data(TC_ID,"source_stack");
				String Mg_token = Excel_Handling.Get_Data(TC_ID,"management_token");
				String path = Excel_Handling.Get_Data(TC_ID,"data_path");
				String alias = Excel_Handling.Get_Data(TC_ID,"alias");
				String source_env = Excel_Handling.Get_Data(TC_ID,"source_env");
				String cont_Types = Excel_Handling.Get_Data(TC_ID,"cont_Types").replace("|", "");
				String env = Excel_Handling.Get_Data(TC_ID,"env").replace("|", "");
				String key = "blt49d5f9742f4c7d2b";

				String currentDir = System.getProperty("user.dir");

				//API_call
				
				API_calls.Create_Management_token(TC_ID, driver, key);
				//String M_token = API_calls.M_Token;
				method.Mg_token(driver, "csdx auth:tokens:add -m ", alias, key, API_calls.M_Token, " successfully", "Management token added successfully >>");
				
				//Entries
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("entries")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk Publish Entries command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:entries " + " -t " + "blog_post" + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("	<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				} 
				//Assets
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("assets")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk Publish Assets command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:assets " + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}

				//Entry-Edits
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("entry-edits")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk Publish Entry edits command ", driver);
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:entry-edits " + " -t " + cont_Types + " -s "+ source_env + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (( fileAsStringb.contains("No Edits Were observed")|| fileAsStringb.contains("The success log for this session is stored at"))) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//Cross-publish
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("cross-publish")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk Publish Cross Publish command ", driver);
					String dev_tok = API_calls.get_delivery_token(TC_ID, driver, key);
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:cross-publish  " + " -t " + cont_Types + " -e " + source_env + " -d "+ env +" -l " + locale + " -a " +  alias + " -x " +dev_tok+" -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//unpublish
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("unpublish")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk Unpublish command ", driver);
					String dev_tok = API_calls.get_delivery_token(TC_ID, driver, key);
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:unpublish  " + " -t " + cont_Types + " -e " + source_env + " -l " + locale + " -a " +  alias + " -x " +dev_tok +" -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//unpublished-entries
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("unpublished-entries")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk Unpublish Entries command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:unpublished-entries  " + " -t " + cont_Types + " -s "+source_env+" -e "+ env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//add-fields
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("add-fields")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk add fieldsh command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:add-fields  " + " -t " + cont_Types + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//nonlocalized-field-changes
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("nonlocalized-field-changes")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Bulk nonlocalized-field-changes command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:nonlocalized-field-changes  " + " -t " + cont_Types + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//configure
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("configure")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing cofigure command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:configure  " + " -t " + cont_Types + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//clear
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("clear")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing clear command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:clear  " + " -t " + cont_Types + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
					} 
				}
				//revert
				if (Excel_Handling.Get_Data(TC_ID,"sub_module").contains("revert")) 
				{
					Extent_Reporting.Log_Pass(" Test case started"," Testing Revert command ", driver);

					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx cm:bulk-publish:revert  " + " -t " + cont_Types + " -e " + env + " -l " + locale + " -a " +  alias + " -y"+ " >bulk-op.txt");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\bulk-op.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					if (fileAsStringb.contains("The success log for this session is stored at")) {
						System.out.println("<<<<<<<<<<< Test case passed >>>>>>>>>>>>");
						Extent_Reporting.Log_Pass(" Test case is passed.."," Test case is passed & the success log is >> " + fileAsStringb, driver);
					} else {
						System.out.println("   ------------ Test case Failed -----------");
						Extent_Reporting.Log_Fail("Test case failed..", " Test case is Failed & the failure log is >>  "+ fileAsStringb, driver);
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
