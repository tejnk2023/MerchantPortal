package base;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

public class BaseTest {
    public WebDriver driver;
    public Properties p = new Properties();
    public FluentWait<WebDriver> wait;
    public SoftAssert softassert;

    public String testurl;
    public String UserName;
    public String pwd;

    @BeforeClass
    public void setup() throws IOException {
        loadProperties();

        testurl = getProperty("appURL");
        UserName = getProperty("phoneNum");
        pwd = getProperty("password");

        String browser = getProperty("browser").toLowerCase();

        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("profile.default_content_setting_values.notifications", 2);
                chromeOptions.setExperimentalOption("prefs", chromePrefs);
                driver = new ChromeDriver(chromeOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions); // Edge doesn't support notification suppression well
                break;

            case "firefox":
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("dom.webnotifications.enabled", false);
                profile.setPreference("permissions.default.desktop-notification", 2);
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setProfile(profile);
                driver = new FirefoxDriver(firefoxOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.get(testurl);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(50))
                .pollingEvery(Duration.ofSeconds(1));

        softassert = new SoftAssert();
    }

    @AfterClass
    public void tearDown() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Logout']"))).click();
        } catch (Exception e) {
            System.out.println("Logout not clickable or already logged out.");
        }

        if (driver != null) {
            driver.quit();
        }
    }

    public void login() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='username']"))).sendKeys(UserName);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='password']"))).sendKeys(pwd);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
    }

    private void loadProperties() throws IOException {
        try (FileReader fr = new FileReader(System.getProperty("user.dir") + "/src/test/resources/config.properties")) {
            p.load(fr);
        } catch (IOException e) {
            throw new IOException("Could not load config.properties file", e);
        }
    }

    private String getProperty(String key) {
        String value = p.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing required property: " + key);
        }
        return value;
    }
}
