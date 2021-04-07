package cli.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import io.github.bonigarcia.wdm.WebDriverManager;

import cli.Reporting.Extent_Reporting;


public class Driver_Setup {
	
	public WebDriver driver;
	public String driverPath = Constants.driverPath;
    public String TC_ID=null;
    public String Url=null;
    public String browser_Type=null;
	public WebDriver getDriver() {
		
		return driver;
	}

	String currentDir = System.getProperty("user.dir");
	String path = currentDir+"\\Exported_content";
	
	public void setDriver(String browserType, String appURL) {
		
		switch(browserType) {
		case "IE":
			driver = initIEDriver(appURL);
			break;
		case "CHROME":
			driver = initChromeDriver(appURL);
			break;
		case "FIREFOX":
			driver = initFirefoxDriver(appURL);
			break;
		default:
			System.out.println("browser : " + browserType
					+ " is invalid, Launching Firefox as default for execution...");
			driver = initFirefoxDriver(appURL);
		}
	}

	@SuppressWarnings("deprecation")
	public WebDriver initIEDriver(String appURL)  {
		System.out.println("Launching Internet Explorer with new profile..");
		System.setProperty("webdriver.ie.driver", driverPath+ "IEDriverServer.exe");
		DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
		//Log.info(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS);
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		//cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);
		cap.setCapability("nativeEvents", false);    
		cap.setCapability("unexpectedAlertBehaviour", "accept");
		cap.setCapability("ignoreProtectedModeSettings", true);
		cap.setCapability("disable-popup-blocking", true);
		cap.setCapability("enablePersistentHover", true);
		cap.setCapability("ignoreZoomSetting", true);
		cap.setJavascriptEnabled(true);	
		driver = new InternetExplorerDriver(cap);
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*WebDriverWait wait = new WebDriverWait(driver, 20);      
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());     
		alert.authenticateUsing(new UserAndPassword("INOS006516", "May@2018"));
		
		try{
		for(int i=1;i<=10;i++){
			Alert alert1 = wait.until(ExpectedConditions.alertIsPresent());
			alert1.dismiss();
		}
		}catch(Exception e){
			
		}*/
		
		return driver;
	}
	
	private WebDriver initChromeDriver(String appURL) {
		System.out.println("Launching Headless google chrome with new profile..");		
		System.setProperty("webdriver.chrome.driver", driverPath+ "chromedriver.exe");
	    WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--no-sandbox");
	    chromeOptions.addArguments("--disable-dev-shm-usage");
	    chromeOptions.addArguments("--headless");
	    
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		Extent_Reporting.Log_Pass("Headless Chrome launched", "Headless browser instance is launched", driver);
		return driver;
	}

	private WebDriver initFirefoxDriver(String appURL) {
		System.out.println("Launching Firefox browser..");
		System.setProperty("webdriver.gecko.driver", driverPath+ "geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}

	@Parameters({ "browserType", "appURL","tcID" })
	@BeforeClass
	public void initializeTestBaseSetup(String browserType, String appURL,String tcID) {
		try {
			
			setDriver(browserType.toUpperCase(), appURL);
			TC_ID=tcID;
			Url=appURL;
			browser_Type=browserType.toUpperCase();

		} catch (Exception e) {
			System.out.println("Error....." + e.getStackTrace());
		}
	}
	
	@AfterClass
	public void tearDown() throws IOException {
		
		driver.quit();
		
	}
	

}
