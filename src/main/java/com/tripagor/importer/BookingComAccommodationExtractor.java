package com.tripagor.importer;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripagor.importer.model.Accommodation;

public class BookingComAccommodationExtractor {

	private WebDriver driver;
	private final Logger logger = LoggerFactory.getLogger(BookingComAccommodationExtractor.class);

	public BookingComAccommodationExtractor() {
	}

	public Accommodation extract(String url) throws RuntimeException {
		try {
			driver = new FirefoxDriver();
			Accommodation result = new Accommodation();

			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			URL urlObj = new URL(url.replace("http:/", "http://"));
			driver.get(urlObj.toString());
			WebElement address = driver.findElement(By.className("hp_address_subtitle"));
			WebElement desc = driver.findElement(By.id("summary"));
			WebElement title = driver.findElement(By.id("hp_hotel_name"));
			
			driver.close();

			result.setUrl(url);
			result.setDescription(desc.getText());
			result.setName(title.getText());
			result.setAddress(address.getText());

			return result;
		} catch (Exception e) {
			throw new RuntimeException("error loading page" + e);
		}
	}

}
