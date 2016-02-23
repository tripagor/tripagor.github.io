package com.tripagor.importer;

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

	public Accommodation extract(String url) {
		driver = new FirefoxDriver();
		try {
			Accommodation result = new Accommodation();

			driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(url);
			WebElement address = driver.findElement(By.className("hp_address_subtitle"));
			WebElement desc = driver.findElement(By.id("summary"));
			WebElement title = driver.findElement(By.id("hp_hotel_name"));

			result.setUrl(url);
			result.setDescription(desc.getText());
			result.setName(title.getText());
			result.setAddress(address.getText());

			return result;
		} catch (Exception e) {
			logger.error("could not extract data from {}", url);
			return null;
		} finally {
			driver.close();
		}
	}

}
