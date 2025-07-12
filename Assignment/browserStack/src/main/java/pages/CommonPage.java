package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonPage {

    WebDriver driver;

    // Constructor to receive WebDriver from the test class
    public CommonPage(WebDriver driver) {
        this.driver = driver;
    }

    public void handleCookiePopupIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement acceptBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("didomi-notice-agree-button"))
            );
            if (acceptBtn.isDisplayed()) {
                acceptBtn.click();
                System.out.println("✅ Accepted cookie banner.");
                Thread.sleep(500); // Optional delay to allow DOM update
            }
        } catch (Exception e) {
            // It's okay if not found
            System.out.println("ℹ️ No cookie banner to accept.");
        }
    }
}
