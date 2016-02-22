package com.tripagor.util.extrator;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.tripagor.util.extrator.model.Accommodation;

public class BookingComAccommodationExtractor {
	
	private WebDriver driver;
	
	public BookingComAccommodationExtractor() {
		driver = new FirefoxDriver();
	}

	public Accommodation extract(String url) {
		Accommodation result = new Accommodation();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(url);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement address = driver.findElement(By.className("hp_address_subtitle"));
		WebElement desc = driver.findElement(By.id("summary"));
		WebElement title = driver.findElement(By.id("hp_hotel_name"));

		driver.close();
		
		
		result.setUrl(url);
		result.setDescription(desc.getText());
		result.setTitle(title.getText());
		result.setAddress(address.getText());
		
		return result;
	}

}
