package com.tripagor.cli.exporter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class KeywordExporterSeleniumImpl {
	private WebDriver driver;
	private static final int GOOGLE_KEYWORD_MAX_SIZE = 100;
	private Path targetPath;

	public KeywordExporterSeleniumImpl(String username, String password, String target) {
		super();
		try {
			targetPath = Paths.get(target, UUID.randomUUID().toString());
			if (!Files.exists(Paths.get(target))) {
				Files.createDirectories(targetPath);
			}
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("browser.download.dir", targetPath.toAbsolutePath().toString());

			this.driver = new FirefoxDriver(profile);

			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

			driver.get("https://adwords.google.com/KeywordPlanner");

			driver.findElement(By.linkText("Anmelden")).click();
			driver.findElement(By.id("Email")).clear();
			driver.findElement(By.id("Email")).sendKeys(username);
			driver.findElement(By.id("next")).click();
			driver.findElement(By.id("Passwd")).clear();
			driver.findElement(By.id("Passwd")).sendKeys(password);
			driver.findElement(By.id("signIn")).click();

			driver.findElement(By.cssSelector("#gwt-debug-splash-panel-stats-selection-input > div.gwt-Label.spgf-i"))
					.click();
			driver.findElement(By.cssSelector("#gwt-uid-27 > #gwt-debug-upload-text-box")).click();
			driver.findElement(By.cssSelector("#gwt-uid-27 > #gwt-debug-upload-text-box")).clear();
			driver.findElement(By.cssSelector("#gwt-uid-27 > #gwt-debug-upload-text-box")).sendKeys("blubb");
			driver.findElement(By.cssSelector(
					"#gwt-debug-stats-selection-splash-panel-form > #gwt-debug-action-buttons > div > #gwt-debug-upload-ideas-button > div.goog-button-base-outer-box.goog-inline-block > div.goog-button-base-inner-box.goog-inline-block > div.goog-button-base-pos > div.goog-button-base-content > #gwt-debug-upload-ideas-button-content"))
					.click();

		} catch (Exception e) {
			throw new RuntimeException("could nost start browser", e);
		}

	}

	public List<File> doExport(List<String> keywords) throws Exception {

		int currentChunk = 0;
		boolean isLast = false;
		while (!isLast) {
			String keywordsStr = "";
			int start = currentChunk * GOOGLE_KEYWORD_MAX_SIZE;
			int end = (currentChunk + 1) * GOOGLE_KEYWORD_MAX_SIZE;
			if (end > keywords.size()) {
				end = keywords.size();
				isLast = true;
			}

			for (String keyword : keywords.subList(start, end)) {
				keywordsStr += keyword + "\n";
			}

			System.out.println(keywordsStr);
			currentChunk++;

			driver.findElement(By.cssSelector(
					"#gwt-debug-modify-search-button > div.goog-button-base-outer-box.goog-inline-block > div.goog-button-base-inner-box.goog-inline-block > div.goog-button-base-pos > div.goog-button-base-content"))
					.click();
			driver.findElement(By.cssSelector("#gwt-uid-1395 > #gwt-debug-upload-text-box")).clear();
			driver.findElement(By.cssSelector("#gwt-uid-1395 > #gwt-debug-upload-text-box")).sendKeys(keywordsStr);
			driver.findElement(By.xpath("(//span[@id='gwt-debug-upload-ideas-button-content'])[7]")).click();
			Thread.sleep(2500);
			driver.findElement(By.xpath("//div[@id='gwt-debug-search-download-button']/div[2]/div/div/div/div/div[2]"))
					.click();
			driver.findElement(By.cssSelector(
					"#gwt-debug-download-button > div.goog-button-base-outer-box.goog-inline-block > div.goog-button-base-inner-box.goog-inline-block > div.goog-button-base-pos > div.goog-button-base-content"))
					.click();
			driver.findElement(By.id("gwt-debug-retrieve-download-content")).click();
		}

		return Arrays.asList(this.targetPath.toFile().listFiles());
	}

}
