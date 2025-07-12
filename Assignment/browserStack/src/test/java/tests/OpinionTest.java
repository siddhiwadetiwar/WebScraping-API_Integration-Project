package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import base.BrowserStackBaseTest;
import pages.CommonPage;
import utils.ImageDownloader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpinionTest extends BrowserStackBaseTest {

    @Test
    public void scrapeTopFiveOpinionArticles() throws InterruptedException {
        driver.get("https://elpais.com/opinion/");
        System.out.println("🔎 Navigated to Opinion Section...");

        CommonPage commonPage = new CommonPage(driver);  // pass your WebDriver instance
        commonPage.handleCookiePopupIfPresent();      

        // Step 1: Collect first 5 articles' titles and URLs from <article> blocks
        List<WebElement> articleBlocks = driver.findElements(By.xpath("//article"));
        int count = Math.min(articleBlocks.size(), 5);
        System.out.println("✅ Found " + count + " articles.");

        List<String> titles = new ArrayList<>();
        List<String> urls = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            try {
                WebElement h2 = articleBlocks.get(i).findElement(By.xpath(".//h2[contains(@class,'c_t')] | .//h2"));
                WebElement aTag = h2.findElement(By.xpath(".//a"));
                String titleText = h2.getText().trim();
                String articleUrl = aTag.getAttribute("href");

                titles.add(titleText);
                urls.add(articleUrl);
            } catch (Exception e) {
                System.out.println("❌ Failed to extract title/URL for article " + (i + 1) + ": " + e.getMessage());
                titles.add("[Title Missing]");
                urls.add("");
            }
        }

        // Step 2: Visit each URL one-by-one
        for (int i = 0; i < count; i++) {
            System.out.println("\n📰 Article " + (i + 1) + " Title: " + titles.get(i));
            System.out.println("🔗 URL: " + urls.get(i));

            if (urls.get(i).isEmpty()) {
                System.out.println("⚠️ Skipping article due to missing URL.");
                continue;
            }

            driver.navigate().to(urls.get(i));
            Thread.sleep(2000);
            commonPage.handleCookiePopupIfPresent();    

            try {
                WebElement contentElement = driver.findElement(By.cssSelector("div.a_c.clearfix"));
                String content = contentElement.getText();
                System.out.println(content);
            } catch (Exception e) {
                System.out.println("⚠️ Could not fetch content: " + e.getMessage());
            }

            // Step 3: Download image if exists
            try {
                WebElement img = driver.findElement(By.cssSelector("figure img"));
                String imgUrl = img.getAttribute("src");
                String browser = System.getProperty("browser", "unknown");
                String fileName = "downloads/" + browser + "_article_" + (i + 1) + "_" + UUID.randomUUID() + ".jpg";
                System.out.println("🖼️ Cover image URL: " + imgUrl);
                ImageDownloader.downloadImage(imgUrl, fileName);
            } catch (Exception e) {
                System.out.println("⚠️ No image found for article " + (i + 1));
            }
        }
    }
}


