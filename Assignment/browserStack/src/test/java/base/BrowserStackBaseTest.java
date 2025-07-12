package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.URL;
import java.util.HashMap;

public class BrowserStackBaseTest {
    protected WebDriver driver;

    @Parameters({"os", "os_version", "browser", "browser_version", "device"})
    @BeforeMethod
    public void setUp(@Optional("Windows") String os,
                      @Optional("11") String os_version,
                      @Optional("chrome") String browser,
                      @Optional("latest") String browser_version,
                      @Optional("") String device) throws Exception {

        MutableCapabilities options;

        // Create options based on desktop or mobile
        if (!device.isEmpty()) {
            options = new MutableCapabilities();
            options.setCapability("browserName", browser);
        } else {
            // You can extend this to use FirefoxOptions, EdgeOptions if needed
            options = new ChromeOptions(); // Default
        }

        HashMap<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", os);
        bstackOptions.put("osVersion", os_version);
        bstackOptions.put("browserVersion", browser_version);
        bstackOptions.put("projectName", "El Pais Testing");
        bstackOptions.put("buildName", "CrossBrowserSuite");
        bstackOptions.put("sessionName", "Parallel Test");
        bstackOptions.put("userName", "siddhiwadetiwar_QLBeAb");
        bstackOptions.put("accessKey", "2bjFcWMcrnw62GRBEYxq");

        if (!device.isEmpty()) {
            bstackOptions.put("deviceName", device);
            bstackOptions.put("realMobile", true);
        }

        options.setCapability("bstack:options", bstackOptions);

        // ✅ Correct WebDriver hub URL
        driver = new RemoteWebDriver(
            new URL("https://siddhiwadetiwar_QLBeAb:2bjFcWMcrnw62GRBEYxq@hub.browserstack.com/wd/hub"), options);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            // Determine the status
            String status = result.getStatus() == ITestResult.SUCCESS ? "passed" : "failed";
            String reason = result.getStatus() == ITestResult.SUCCESS ?
                    "Assertions passed" :
                    result.getThrowable() != null ? result.getThrowable().getMessage() : "Test failed";

            // Report status to BrowserStack
            ((JavascriptExecutor) driver).executeScript(
                    "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": " +
                            "{\"status\":\"" + status + "\", \"reason\": \"" + reason + "\"}}");

            // Close the session
            driver.quit();
        }
    }
}
