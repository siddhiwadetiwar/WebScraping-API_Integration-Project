package tests;


import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import base.BrowserStackBaseTest;
import pages.CommonPage;
import utils.TranslatorUtil;

import java.time.Duration;
import java.util.*;

public class TranslateTitleTest extends BrowserStackBaseTest {

    // ✅ Store translated titles for reuse
    private static final List<String> translatedTitles = new ArrayList<>();

    @Test
    public void translateArticleTitlesAndAnalyze() throws InterruptedException {
        driver.get("https://elpais.com/opinion/");
        System.out.println("🌐 Navigated to Opinion Section...");

        CommonPage commonPage = new CommonPage(driver);
        commonPage.handleCookiePopupIfPresent();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 6; i++) {
            js.executeScript("window.scrollBy(0, 500);");
            Thread.sleep(1000);
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//article")));

        List<WebElement> articles = driver.findElements(By.xpath("//article"));
        int count = Math.min(articles.size(), 5); // ✅ Limit to 5
        System.out.println("✅ Found " + articles.size() + " articles. Processing first " + count + ".\n");

        for (int i = 0; i < count; i++) {
            try {
                WebElement article = articles.get(i);
                WebElement titleElement;

                try {
                    titleElement = article.findElement(By.xpath(".//h2[contains(@class,'c_t')]"));
                } catch (NoSuchElementException e) {
                    try {
                        titleElement = article.findElement(By.xpath(".//h2"));
                    } catch (NoSuchElementException e2) {
                        System.out.println("⚠️ No title found for article " + (i + 1));
                        continue;
                    }
                }

                String title = titleElement.getText().trim();
                if (title.isEmpty()) {
                    System.out.println("⚠️ Title for article " + (i + 1) + " is empty.");
                    continue;
                }

                // Translate and store
                System.out.println("📰 Spanish Title [" + (i + 1) + "]: " + title);
                String translatedTitle = TranslatorUtil.translate(title, "es", "en");
                System.out.println("🌍 English Translation: " + translatedTitle);

                translatedTitles.add(translatedTitle); // ✅ Add to list

                try {
                    WebElement link = article.findElement(By.xpath(".//a"));
                    String url = link.getAttribute("href");
                    System.out.println("🔗 URL: " + url);
                } catch (Exception ignore) {
                    System.out.println("🔗 URL: No URL found");
                }

                System.out.println();

                if (i < count - 1) {
                    System.out.println("⏸️ Waiting 3 seconds before next article...");
                    Thread.sleep(3000);
                }

            } catch (Exception e) {
                System.out.println("❌ Error processing article " + (i + 1) + ": " + e.getMessage());
            }
        }

        System.out.println("📊 Translated Titles Collected: " + translatedTitles.size());

        // Call analysis logic after translation is done
        analyzeRepeatedWordsInTranslatedTitles();
    }

    public void analyzeRepeatedWordsInTranslatedTitles() {
        // ✅ Normalize and count word frequency
        Map<String, Integer> wordCount = new HashMap<>();
        for (String title : translatedTitles) {
            String[] words = title.toLowerCase().split("\\W+");
            for (String word : words) {
                if (word.isBlank()) continue;
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        // ✅ Print words that appear more than twice
        System.out.println("🔍 Repeated Words (appeared more than twice):");
        boolean found = false;
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > 2) {
                System.out.println(entry.getKey() + " → " + entry.getValue() + " times");
                found = true;
            }
        }

        if (!found) {
            System.out.println("✅ No words repeated more than twice.");
        }
    }
}
