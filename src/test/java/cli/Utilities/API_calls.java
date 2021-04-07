package cli.Utilities;

import org.openqa.selenium.WebDriver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Sleeper;
import cli.Reporting.Extent_Reporting;
import cli.Utilities.Excel_Handling;
import cli.Utilities.WrapperMethods;
import com.google.gson.JsonObject;
import cli.Plugins.SyncPipe;

public class API_calls {

	public WebDriver driver;
	public boolean flag = false;
	public String TC_ID = "";
	static WrapperMethods method = new WrapperMethods();

	public API_calls(WebDriver d, String tcID) {
		this.driver = d;
		this.TC_ID = tcID;
	}
	public static String M_Token;
	public static String API_key;
	public static URL url;
	public static String Dev_token;
	public static String ur;
	public static String uid = "blt44d900d8457ba976";

	public static void Create_stack(String TC_ID, WebDriver driver) throws Throwable
	{

		if (Excel_Handling.Get_Data(TC_ID, "region").contains("stag")) {
			url = new URL("https://stag-api.contentstack.io/v3/stacks");
		}
		if (Excel_Handling.Get_Data(TC_ID, "region").contains("NA")) {
			url = new URL("https://api.contentstack.io/v3/stacks");
		}
		if (Excel_Handling.Get_Data(TC_ID, "region").contains("EU")) {
			url = new URL("https://eu-api.contentstack.io/v3/stacks");
		}

		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("authtoken", "bltdabc909656b8aeec");
		con.setRequestProperty("organization_uid", "blt70ed63a27b83337c");
		con.setDoOutput(true);

		String jsonInputString = "{ \"stack\": { \"name\": \"My Stack\", \"description\": \"My new test stack\", \"master_locale\": \"en-us\" } }";

		try(OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			//System.out.println(response.toString());
			//String r = response.toString();
			String a[] =response.toString().split("api_key") ;
			String b[] = a[1].split(",");
			API_key = b[0].replace("\"", "").replace(":", "");
			System.out.println("New stack API key is --> " + API_key);

		}
	}

	public static void Create_Management_token(String TC_ID, WebDriver driver, String key) throws Throwable
	{
		URL url = new URL ("https://api.contentstack.io/v3/stacks/management_tokens");
		String alias = Excel_Handling.Get_Data(TC_ID,"alias");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("api_key", key);
		con.setRequestProperty("authtoken", "blt0310d9d91eb19ee3");
		con.setDoOutput(true);
		//"blt49d5f9742f4c7d2b"
		String jsonInputString = "{ \"token\": { \"name\": \"" +alias+"\", \"description\": \"This is a sample management token.\", \"scope\": [ { \"module\": \"$all\", \"acl\": { \"read\": true, \"write\": true } } ], \"expires_on\": \"2021-12-10\", \"is_email_notification_enabled\": true } }";

		try(OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println(response.toString());
			String s = response.toString();
			String[] tokens = s.split(":");
			M_Token = tokens[tokens.length-1].replaceAll("}", "").replaceAll("\"", "");
			System.out.println("Management Token is --> " + M_Token);

		}
	}


	public static String get_delivery_token(String TC_ID, WebDriver driver, String key) throws IOException, InterruptedException
	{
		final HttpClient httpClient = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.connectTimeout(Duration.ofSeconds(10))
				.build();
		
		if (Excel_Handling.Get_Data(TC_ID, "region").contains("stag")) {
			ur = "https://stag-api.contentstack.io/v3/stacks/delivery_tokens/"+uid;
		}
		if (Excel_Handling.Get_Data(TC_ID, "region").contains("NA")) {
			ur = "https://api.contentstack.io/v3/stacks/delivery_tokens/"+uid;
		}
		if (Excel_Handling.Get_Data(TC_ID, "region").contains("EU")) {
			ur = "https://eu-api.contentstack.io/v3/stacks/delivery_tokens/"+uid;
		}

		//String url = "https://api.contentstack.io/v3/stacks/delivery_tokens/blt44d900d8457ba976";

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(ur))
				.setHeader("Content-Type", "application/json")
				.setHeader("authtoken", "blt0310d9d91eb19ee3")
				.setHeader("api_key", "blt49d5f9742f4c7d2b")// add request header
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		// print response headers
		//HttpHeaders headers = response.headers();
		//headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

		String repsnceJson= response.body().toString() ;
		String a[] =repsnceJson.split("\"token\"");
		String b[] = a[2].split(",");
		System.out.println("Delivery Token is >>> " +b[0].replace("\"", "").replace(":", "")); 
		return Dev_token = b[0].replace("\"", "").replace(":", "");
	}
}



