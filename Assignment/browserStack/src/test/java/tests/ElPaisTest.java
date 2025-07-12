package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BrowserStackBaseTest;

public class ElPaisTest extends BrowserStackBaseTest {

    @Test
    public void testWebsiteIsInSpanish() {
        driver.get("https://elpais.com");

        WebElement htmlTag = driver.findElement(By.tagName("html"));
        String langAttr = htmlTag.getAttribute("lang");

        System.out.println("Detected lang attribute: " + langAttr);
        Assert.assertEquals(langAttr.toLowerCase(), "es-es", "❌ Language is not Spanish (es-ES)");
        System.out.println("✅ Website language is correctly set to Spanish (es-ES)");
    }

}
