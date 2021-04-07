package cli.BusinessFlows;

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


public class Sample {


	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	WrapperMethods method = new WrapperMethods();


	public Sample(WebDriver d, String tcID) {
		this.driver = d;
		this.TC_ID = tcID;
	}

	public static String os = System.getProperty("os.name").toLowerCase();

	public static void main(String TC_ID, WebDriver driver) throws Throwable
	{		

		if (os.contains("windows")) {
			try {

				String[] command ={ "cmd",};
				Process p;
				
				String alias = Excel_Handling.Get_Data(TC_ID, "alias");
				String currentDir = System.getProperty("user.dir");

				String repo = "https://github.com/contentstack/cli.git";
				String branch = "optimization-import";

				//checking if CLI repo is already cloned
				String pathR = currentDir + "/cli";
				File fileR = new File(pathR);
				if (!fileR.exists()) {
					System.out.println("Setting up CLI by cloning git repo...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("git clone " + repo);
					stdin.println("cd cli");
					stdin.println("git checkout " + branch);

					stdin.close();
					p.waitFor();
					Thread.sleep(500);

					System.out.println(" CLI by cloning git repo cloned & ready to be tested...");
					Extent_Reporting.Log_Pass(" CLI git repo"," CLI git repo is cloned successfully & checked out on  banch -> " + branch, driver);

				} else {
					System.out.println(" CLI git repo is already cloned...");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		if (os.contains("linux")) {
			try {

				String[] command ={ "/bin/bash",};
				Process p;
				String alias = Excel_Handling.Get_Data(TC_ID, "alias");
				String currentDir = System.getProperty("user.dir");

				String repo = "https://github.com/contentstack/cli.git";
				String branch = "optimization-import";

				//checking if CLI repo is already cloned
				String pathR = currentDir + "/cli";
				File fileR = new File(pathR);
				if (!fileR.exists()) {
					System.out.println("Setting up CLI by cloning git repo...");
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("git clone " + repo);
					stdin.println("cd cli");
					stdin.println("git checkout " + branch);

					stdin.close();
					p.waitFor();
					Thread.sleep(500);

					System.out.println(" CLI by cloning git repo cloned & ready to be tested...");
					Extent_Reporting.Log_Pass(" CLI git repo",
							" CLI git repo is cloned successfully & checked out on  banch -> " + branch, driver);

				} else {
					System.out.println(" CLI git repo is already cloned...");
				}

			} catch (Exception e) {
				e.printStackTrace();
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

	}

}
