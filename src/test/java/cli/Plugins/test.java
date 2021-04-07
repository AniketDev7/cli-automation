package cli.Plugins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.openqa.selenium.WebDriver;

import cli.Reporting.Extent_Reporting;
import cli.Utilities.Excel_Handling;
import cli.Utilities.WrapperMethods;


public class test {


	

	public static void main(String[] args) throws IOException, InterruptedException 
	{		

		try{	

			String[] command ={ "cmd",};
			Process p;
			
			try {

				String locale = "en-us | fr-fr".replace("|", "");
				//String source_stack = Excel_Handling.Get_Data(TC_ID,"source_stack");
				//String Mg_token = "cs_import";
				//String path = Excel_Handling.Get_Data(TC_ID,"data_path");
				String alias = "cs_import";
				String cont_Types = "eng1 | Major".replace("|", "");
				String env = "development | prod".replace("|", "");

				String currentDir = System.getProperty("user.dir");

				
				if ("Entries".contains("Entries")) 
				{
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					
					
					stdin.println("csdx cm:bulk-publish:entries " + " -t " + cont_Types + " -e " + env + " -l " + locale + " -a " +  alias + " -y");
					stdin.close();
					p.waitFor();
					String log_pathb = currentDir+ "\\output.txt";
					BufferedReader brb = new BufferedReader(new FileReader(log_pathb));
					StringBuilder sbb = new StringBuilder();
					String lineb = brb.readLine();
					while (lineb != null) {
						sbb.append(lineb).append("\n");
						lineb = brb.readLine();
					}
					String fileAsStringb = sbb.toString();
					//System.out.println("Text from log ->>" + fileAsString);
					
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






