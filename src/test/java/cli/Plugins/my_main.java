package cli.Plugins;

import java.io.PrintWriter;

public class my_main {
	
	public static void main(String[] args) {
		String[] command =
	    {
	        "cmd",
	    };
	    Process p;
		try {
			p = Runtime.getRuntime().exec(command);
		        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
	                new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
	                PrintWriter stdin = new PrintWriter(p.getOutputStream());
	                stdin.println("cd C:\\Users\\Aniket Shikhare\\Documents\\CLI\\CLI_optimized\\cli");
	                stdin.println("csdx");
	                stdin.println("csdx cm:export -A -l en-us -s blt697efe95acac6bda -d D:\\CLI-export\\Auto_2");

	                stdin.close();
	                p.waitFor();
	    	} catch (Exception e) {
	 		e.printStackTrace();
		}
	}	
}	


