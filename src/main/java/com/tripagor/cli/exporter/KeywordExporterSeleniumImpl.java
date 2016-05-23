package com.tripagor.cli.exporter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class KeywordExporterSeleniumImpl {
	private WebDriver driver;

	public KeywordExporterSeleniumImpl() {
		super();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
		this.driver = new FirefoxDriver(profile);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://adwords.google.com/KeywordPlanner");
	}

	public void doExport(List<String> keywords) {
		String keywordsStr = "";
		for(String keyword:keywords){
			keywordsStr += keyword+"\n";
		}
		driver.findElement(By.linkText("Anmelden")).click();
		driver.findElement(By.id("Email")).clear();
		driver.findElement(By.id("Email")).sendKeys("admin@pickito.de");
		driver.findElement(By.id("next")).click();
		driver.findElement(By.id("Passwd")).clear();
		driver.findElement(By.id("Passwd")).sendKeys("sensor2016");
		driver.findElement(By.id("signIn")).click();
		driver.findElement(By.cssSelector("#gwt-debug-splash-panel-stats-selection-input > div.gwt-Label.spdf-i"))
				.click();
		driver.findElement(By.cssSelector("#gwt-uid-27 > #gwt-debug-upload-text-box")).clear();
		driver.findElement(By.cssSelector("#gwt-uid-27 > #gwt-debug-upload-text-box")).sendKeys(keywordsStr);
		driver.findElement(By.cssSelector(
				"#gwt-debug-stats-selection-splash-panel-form > #gwt-debug-action-buttons > div > #gwt-debug-upload-ideas-button > div.goog-button-base-outer-box.goog-inline-block > div.goog-button-base-inner-box.goog-inline-block > div.goog-button-base-pos > div.goog-button-base-content"))
				.click();
		driver.findElement(By.xpath("//div[@id='gwt-debug-search-download-button']/div[2]/div/div/div/div/div[2]/span"))
				.click();
		driver.findElement(By.id("gwt-debug-download-button-content")).click();
		driver.findElement(By.id("gwt-debug-retrieve-download-content")).click();
	}

	public static void main(String[] args) {
		
		KeywordExporterSeleniumImpl exporter = new KeywordExporterSeleniumImpl();
		exporter.doExport(Arrays.asList(new String[] { "hotels fürth", "hotels nürnberg" }));
	}
}
