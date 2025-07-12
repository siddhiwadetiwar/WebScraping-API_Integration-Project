# 🧪 El País Opinion - Cross-Browser Automation & Translation Suite

This is a QA Automation project I built to demonstrate my ability to write scalable automation tests involving **web scraping**, **API integration**, and **cross-browser testing** using Selenium and BrowserStack.

---

## 📌 Project Goals

- ✅ Verify that the [El País](https://elpais.com/) homepage is correctly displayed in Spanish.
- 📰 Scrape the **top 5 articles** from the Opinion section (title, content, and associated image).
- 🌐 Translate the scraped article titles from **Spanish to English** using a **Translation API**.
- 🔄 Run **parallel cross-browser** test execution on **BrowserStack**.

---

## 🚀 Technologies Used

| Tech | Purpose |
|------|---------|
| Java | Core programming language |
| Selenium WebDriver | Browser automation |
| TestNG | Test execution & reporting |
| Maven | Dependency and build management |
| BrowserStack Automate | Cross-browser cloud testing |
| RapidAPI Translation API | Title translation |
| Page Object Model (POM) | Scalable test structure |

---

## 🖥️ Project Structure

📁 src
┣ 📁 main
┃ ┗ 📁 java
┃ ┣ 📁 base # BrowserStack config base class
┃ ┣ 📁 pages # Page Object Model for reusable elements
┃ ┗ 📁 utils # Utility classes (e.g., translation, image downloader)
┣ 📁 test
┃ ┗ 📁 java
┃ ┗ 📁 tests # TestNG test classes
📄 pom.xml # Project dependencies

---

## ✅ Test Summary

- Executed tests across **5 parallel threads** using different browser/OS combinations.
- All tests passed successfully, including:
  - Language check
  - Article scraping
  - API translation
  - Cross-browser validations

📊 **Live Test Report (BrowserStack Automate Build)**  
🔗 [View Full Test Build]:
(https://automate.browserstack.com/projects/El+Pais+Testing/builds/CrossBrowserSuite/5?public_token=8d2a441a07b3dd33b143048b682b71c2998e6f6ef9bcc46b632d381c977e1ff5)

🖼️ **Screenshot of Build Running**  
📷 [View on Google Drive]:
(https://drive.google.com/drive/folders/1JgtmAVm0RtyDLVYAwaah6syVXtj-VPeX?usp=drive_link)

---

## ⚙️ How to Run Locally

1. Clone this repository:
   ```bash
   https://github.com/siddhiwadetiwar/WebScraping-API_Integration-Project.git

