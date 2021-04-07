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
import org.openqa.selenium.support.ui.Sleeper;


import cli.Reporting.Extent_Reporting;
import cli.Utilities.Excel_Handling;
import cli.Utilities.WrapperMethods;
import cli.Plugins.SyncPipe;


public class Auth_script {


	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	WrapperMethods method = new WrapperMethods();


	public Auth_script(WebDriver d, String tcID) {
		this.driver = d;
		this.TC_ID = tcID;
	}

	static String os = System.getProperty("os.name").toLowerCase();

	public static void Execute(String TC_ID, WebDriver driver) throws Throwable
	{		

		String env = Excel_Handling.Get_Data(TC_ID,"env");
		String source_stack = Excel_Handling.Get_Data(TC_ID,"source_stack");
		String Mg_token = Excel_Handling.Get_Data(TC_ID,"token");
		String path = Excel_Handling.Get_Data(TC_ID,"data_path");

		String alias = Excel_Handling.Get_Data(TC_ID,"alias");
		String currentDir = System.getProperty("user.dir");

		try {
			//Auth plugin's Test cases

			if (os.contains("windows")) {
				String[] command = { "cmd", };
				Process p;

				//1. Login
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").contains("login")) {
					System.out.println("Executing Login test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("csdx login -u " + Excel_Handling.Get_Data(TC_ID, "username") + " >output1.txt");
					stdin.println(Excel_Handling.Get_Data(TC_ID, "password"));
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("Contentstack account authenticated successfully!")) {

						System.out.println("Logged in successfully to CLI");
						Extent_Reporting.Log_Pass(" Login ", " Logged in successfully to CLI >> ", driver);

					} else {
						System.out.println("Auth token adding failed -----------");
						Extent_Reporting.Log_Fail("Login failed..", "Login failed with this error >> " + m_token,driver);

					}
				}
				//2. Logout
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").contains("logout")) {
					System.out.println("Executing Logout test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("csdx logout -f " + ">output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("You have logged out from Contentstack successfully!")) {

						System.out.println("Logged out from CLI");
						Extent_Reporting.Log_Pass(" Logout ", " logged out from Contentstack CLI successfully! >> ",driver);

					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail("Logout failed..", "Logout failed with this error >> " + m_token,driver);

					}
				}
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").contains("tokens:add")) {
					System.out.println("Executing Tokens adding  test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					if (Excel_Handling.Get_Data(TC_ID, "token_typ").contains("Management")) {
						stdin.println("csdx auth:tokens:add -m -k " + Excel_Handling.Get_Data(TC_ID, "stack_key")
						+ " -a " + Excel_Handling.Get_Data(TC_ID, "alias") + " -t "
						+ Excel_Handling.Get_Data(TC_ID, "token") + " -f" + ">output1.txt");
					}
					if (Excel_Handling.Get_Data(TC_ID, "token_typ").contains("Delivery")) {
						stdin.println("csdx auth:tokens:add -d -k " + Excel_Handling.Get_Data(TC_ID, "stack_key")
						+ " -a " + Excel_Handling.Get_Data(TC_ID, "alias") + " -t "
						+ Excel_Handling.Get_Data(TC_ID, "token") + " -e "
						+ Excel_Handling.Get_Data(TC_ID, "env") + " -f" + ">output1.txt");
					}

					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("token added successfully!")) {

						System.out.println("token added successfully!");
						if (Excel_Handling.Get_Data(TC_ID, "token_typ").contains("Management")) {
							Extent_Reporting.Log_Pass(" Management Token added succesfully "," Management token added successfully! ", driver);
						} else {
							Extent_Reporting.Log_Pass(" Delivery Token added succesfully "," Delivery token added successfully! ", driver);
						}

					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail("Token adding..", "Token adding failed with this error >> " + m_token,driver);

					}
				}
				//4. ALl tokens listing
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").equalsIgnoreCase("tokens")) {
					System.out.println("Executing Tokens listing  test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx auth:tokens " + ">output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains(Excel_Handling.Get_Data(TC_ID, "alias"))) {

						System.out.println("Tokens list is displayed");
						Extent_Reporting.Log_Pass(" Token listing ", " Tokens are listed !", driver);
					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail(" Token listing ",
								"Token listing failed with this error >> " + m_token, driver);

					}
				}
				//5. Removing a token
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").equalsIgnoreCase("tokens:renove")) {
					System.out.println("Executing Removing a token test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("csdx auth:tokens:remove -a " + Excel_Handling.Get_Data(TC_ID, "alias") + " >output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("token removed successfully!")) {

						System.out.println("Tokens is removed");
						Extent_Reporting.Log_Pass(" Token listing ", " Tokens is removed !", driver);
					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail(" Token listing ",
								"Token removing failed with this error >> " + m_token, driver);

					}
				}
				//6. Whoami
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").equalsIgnoreCase("whoami")) {
					System.out.println("Executing Whoami test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx whoami " + ">output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("You are currently logged in with email")) {

						System.out.println("Logged in user info displayed");
						Extent_Reporting.Log_Pass(" whoami ", " Logged in user info displayed ", driver);
					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail(" whoami ","Logged in user info display failed with this error >> " + m_token, driver);

					}
				} 
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//Auth plugin's Test cases on Linux/Mac OS

			if (os.contains("linux")) {
				String[] command = { "/bin/bash",};
				Process p;

				//1. Login
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").contains("login")) {
					System.out.println("Executing Login test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("csdx login -u " + Excel_Handling.Get_Data(TC_ID, "username") + " >output1.txt");
					stdin.println(Excel_Handling.Get_Data(TC_ID, "password"));
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("Contentstack account authenticated successfully!")) {

						System.out.println("Logged in successfully to CLI");
						Extent_Reporting.Log_Pass(" Login ", " Logged in successfully to CLI >> ", driver);

					} else {
						System.out.println("Auth token adding failed -----------");
						Extent_Reporting.Log_Fail("Login failed..", "Login failed with this error >> " + m_token,driver);

					}
				}
				//2. Logout
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").contains("logout")) {
					System.out.println("Executing Logout test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("csdx logout -f " + ">output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("You have logged out from Contentstack successfully!")) {

						System.out.println("Logged out from CLI");
						Extent_Reporting.Log_Pass(" Logout ", " logged out from Contentstack CLI successfully! >> ",driver);

					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail("Logout failed..", "Logout failed with this error >> " + m_token,driver);

					}
				}
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").contains("tokens:add")) {
					System.out.println("Executing Tokens adding  test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					if (Excel_Handling.Get_Data(TC_ID, "token_typ").contains("Management")) {
						stdin.println("csdx auth:tokens:add -m -k " + Excel_Handling.Get_Data(TC_ID, "stack_key")
						+ " -a " + Excel_Handling.Get_Data(TC_ID, "alias") + " -t "
						+ Excel_Handling.Get_Data(TC_ID, "token") + " -f" + ">output1.txt");
					}
					if (Excel_Handling.Get_Data(TC_ID, "token_typ").contains("Delivery")) {
						stdin.println("csdx auth:tokens:add -d -k " + Excel_Handling.Get_Data(TC_ID, "stack_key")
						+ " -a " + Excel_Handling.Get_Data(TC_ID, "alias") + " -t "
						+ Excel_Handling.Get_Data(TC_ID, "token") + " -e "
						+ Excel_Handling.Get_Data(TC_ID, "env") + " -f" + ">output1.txt");
					}

					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("token added successfully!")) {

						System.out.println("token added successfully!");
						if (Excel_Handling.Get_Data(TC_ID, "token_typ").contains("Management")) {
							Extent_Reporting.Log_Pass(" Management Token added succesfully "," Management token added successfully! ", driver);
						} else {
							Extent_Reporting.Log_Pass(" Delivery Token added succesfully "," Delivery token added successfully! ", driver);

						}

					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail("Token adding..", "Token adding failed with this error >> " + m_token,driver);

					}
				}
				//4. ALl tokens listing
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").equalsIgnoreCase("tokens")) {
					System.out.println("Executing Tokens listing  test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx auth:tokens " + ">output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains(Excel_Handling.Get_Data(TC_ID, "alias"))) {

						System.out.println("Tokens list is displayed");
						Extent_Reporting.Log_Pass(" Token listing ", " Tokens are listed !", driver);
					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail(" Token listing ",
								"Token listing failed with this error >> " + m_token, driver);

					}
				}
				//5. Removing a token
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").equalsIgnoreCase("tokens:renove")) {
					System.out.println("Executing Removing a token test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx auth:tokens:remove -a " + Excel_Handling.Get_Data(TC_ID, "alias") + " >output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("token removed successfully!")) {

						System.out.println("Tokens is removed");
						Extent_Reporting.Log_Pass(" Token listing ", " Tokens is removed !", driver);
					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail(" Token listing ","Token removing failed with this error >> " + m_token, driver);

					}
				}
				//6. Whoami
				if (Excel_Handling.Get_Data(TC_ID, "sub_module").equalsIgnoreCase("whoami")) {
					System.out.println("Executing Whoami test case...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("csdx whoami " + ">output1.txt");
					stdin.close();
					p.waitFor();
					Thread.sleep(500);
					//Reading & verifying the console msg of above command
					String log_path1 = currentDir + "\\output1.txt";
					BufferedReader br1 = new BufferedReader(new FileReader(log_path1));
					StringBuilder sb1 = new StringBuilder();
					String line1 = br1.readLine();
					while (line1 != null) {
						sb1.append(line1).append("\n");
						line1 = br1.readLine();
					}
					String m_token = sb1.toString();
					if (m_token.contains("You are currently logged in with email")) {

						System.out.println("Logged in user info displayed");
						Extent_Reporting.Log_Pass(" whoami ", " Logged in user info displayed ", driver);
					} else {
						System.out.println("Log out failed -----------");
						Extent_Reporting.Log_Fail(" whoami ","Logged in user info display failed with this error >> " + m_token, driver);

					}
				} 
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
