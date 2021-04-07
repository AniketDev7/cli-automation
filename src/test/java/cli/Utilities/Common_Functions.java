package cli.Utilities;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;

public class Common_Functions {
		
	
	static String snappath=null;	
	public String GetXMLTagValue(String xmlpath, String tagname){
		
		String val=null;

		
		try {
			
			// Method to get the xml tag value from any given xml
				
			File f = new File(xmlpath);
			
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			Document doc = docBuilder.parse(f);
			
			val = doc.getElementsByTagName(tagname).item(0).getTextContent();
			
				
			}
		 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return val;
		
	}
	
	public By locatorParser(String locator) {

		By loc = By.id(locator);

		if (locator.contains("id"))
		    loc = By.id(locator.substring(locator.indexOf("\"") + 1,
		        locator.length() - 2));

		else if (locator.contains("name"))
		    loc = By.name(locator.substring(locator.indexOf("\"") + 1,
		        locator.length() - 2));
		else if (locator.contains("cssSelector"))
		    loc = By.cssSelector(locator.substring(locator.indexOf("\"") + 1,
		        locator.length() - 2));

		if (locator.contains("xpath"))
		    loc = By.xpath(locator.substring(locator.indexOf("\"") + 1,
		        locator.length() - 2));

		return loc;

		}
	
	public static String captureScreenshot(WebDriver driver){
		
		try{
			
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        //The below method will save the screen shot in d drive with name "screenshot.png"
			String name = scrFile.getName();
            FileUtils.copyFile(scrFile, new File(Constants.snapshotsPath+name));
            String newPath = System.getProperty("user.dir") +"/";
            snappath = newPath+Constants.snapshotsPath+name;
            
		}catch(Exception e){
			
			System.out.println("Issue with snapshot capture");
			
		}
		return snappath;
	}
}
