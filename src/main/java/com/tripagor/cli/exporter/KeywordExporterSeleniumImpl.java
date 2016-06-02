package com.tripagor.cli.exporter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class KeywordExporterSeleniumImpl {
	private WebDriver driver;
	private static final int GOOGLE_KEYWORD_MAX_SIZE = 800;

	public KeywordExporterSeleniumImpl(String username, String password, String target) {
		super();
		try {
			if (!Files.exists(Paths.get(target))) {
				Files.createDirectories(Paths.get(target));
			}
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("browser.download.dir", target);

			this.driver = new FirefoxDriver(profile);

			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			driver.get("https:adwords.google.com/KeywordPlanner");

			driver.findElement(By.linkText("Anmelden")).click();
			driver.findElement(By.id("Email")).clear();
			driver.findElement(By.id("Email")).sendKeys(username);
			driver.findElement(By.id("next")).click();
			driver.findElement(By.id("Passwd")).clear();
			driver.findElement(By.id("Passwd")).sendKeys(password);
			driver.findElement(By.id("signIn")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doExport(List<String> keywords) {
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
			driver.findElement(By.cssSelector("#gwt-debug-splash-panel-stats-selection-input > div.gwt-Label.spff-i"))
					.click();
			driver.findElement(By.cssSelector("#gwt-uid-27 > #gwt-debug-upload-text-box")).clear();
			driver.findElement(By.cssSelector("#gwt-uid-27 > #gwt-debug-upload-text-box")).sendKeys(keywordsStr);
			driver.findElement(By.cssSelector(
					"#gwt-debug-stats-selection-splash-panel-form > #gwt-debug-action-buttons > div > #gwt-debug-upload-ideas-button > div.goog-button-base-outer-box.goog-inline-block > div.goog-button-base-inner-box.goog-inline-block > div.goog-button-base-pos > div.goog-button-base-content > #gwt-debug-upload-ideas-button-content"))
					.click();
			driver.findElement(By.xpath("div[@id='gwt-debug-search-download-button']/div[2]/div/div/div/div/div[2]"))
					.click();
			driver.findElement(By.id("gwt-debug-download-button-content")).click();
			driver.findElement(By.cssSelector(
					"#gwt-debug-retrieve-download > div.goog-button-base-outer-box.goog-inline-block > div.goog-button-base-inner-box.goog-inline-block > div.goog-button-base-pos > div.goog-button-base-content"))
					.click();
		}

	}

	public static void main(String[] args) {

		KeywordExporterSeleniumImpl exporter = new KeywordExporterSeleniumImpl("admin@pickito.de", "sensor2016",
				"c:\\temp\\keywords");
		exporter.doExport(Arrays.asList(
				new String[] { "hotels fürth", "hotels nürnberg", "hotels bla", "hotels blubb", "hotels bli" }));
	}
}
