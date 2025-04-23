package base;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

public class BaseTest {
	public static RemoteWebDriver driver;
	public Properties p;
	public static FileReader fr;
	public FluentWait<RemoteWebDriver> wait;
	public static String testurl;
	public static String UserName;
	public static String pwd;
	public SoftAssert softassert;
	
	@BeforeClass
	public void setup() throws IOException {
        // Initialize the Properties object
        p = new Properties();
        
		if(driver==null) {
			try {
				FileReader fr=new FileReader(System.getProperty("user.dir")+ "\\src\\test\\resources\\config.properties");
				p.load(fr);
			} catch (Exception e) {
				System.out.println(e);
			}
			
		}
		
		testurl=p.getProperty("appURL");
		UserName=p.getProperty("phoneNum");
		pwd=p.getProperty("password");
		
		/*if (p.getProperty("browser").equalsIgnoreCase("chrome")) {
			driver=new ChromeDriver();
		} else if (p.getProperty("browser").equalsIgnoreCase("firefox")) {
			driver=new FirefoxDriver();
		} else if (p.getProperty("browser").equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (p.getProperty("browser").equalsIgnoreCase("internetexplorer")) {
			driver = new InternetExplorerDriver();
		} */
		
		/*ChromeOptions options = new ChromeOptions();

        // Disable notifications
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2); // 2 = block
        options.setExperimentalOption("prefs", prefs);
		*/
		//unable to get working 'disable notifications' code for internet explorer as it is a legacy browser
		switch(p.getProperty("browser").toLowerCase()) {
		case "chrome": 
			ChromeOptions options1 = new ChromeOptions();

	        // Disable notifications
	        Map<String, Object> prefs1 = new HashMap<>();
	        prefs1.put("profile.default_content_setting_values.notifications", 2); // 2 = block
	        options1.setExperimentalOption("prefs", prefs1);
			driver=new ChromeDriver(options1);
			break;
		case "edge": 
			EdgeOptions options2 = new EdgeOptions();

	        // Disable notifications
	        Map<String, Object> prefs2 = new HashMap<>();
	        prefs2.put("profile.default_content_setting_values.notifications", 2); // 2 = block
	        options2.setExperimentalOption("prefs", prefs2);
			driver=new EdgeDriver();
			break;
		case "firefox": 
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("dom.webnotifications.enabled", false);
			profile.setPreference("permissions.default.desktop-notification", 2); // 2 = Block

			FirefoxOptions options3 = new FirefoxOptions();
			options3.setProfile(profile);
			driver=new FirefoxDriver(options3); 
			break;

		default: System.out.println("Invalid browser name"); return;
		}
		
		driver.manage().window().maximize();
		driver.get(testurl);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		//Create ExplicitWait object
		wait = new FluentWait<RemoteWebDriver>(driver);
		wait.withTimeout(Duration.ofSeconds(50));
    	wait.pollingEvery(Duration.ofSeconds(1));
		
    	//Create softassert object
    	softassert= new SoftAssert();
	}
	
	@AfterClass
	public void tearDown() {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Logout']"))).click();//Logout of merchant profile
		driver.quit();
	}
	
	public void login() {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='username']"))).sendKeys(UserName);//Enter phone number
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='password']"))).sendKeys(pwd);//Enter password
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();//Click on the submit button
	}

}
