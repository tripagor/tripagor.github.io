package com.tripagor.cli.service;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;

public class PlaceAddApiSeleniumImpl implements PlaceAddApi {
	private String username;
	private String password;

	public PlaceAddApiSeleniumImpl(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public PlaceAddResponse add(PlaceAddRequest place) {
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		PlaceAddResponse placeAddResponse = new PlaceAddResponse();
		try {
			driver.get("http://www.google.de/maps/@50.1109901,8.6828398,15z");
			driver.findElement(By.id("gb_70")).click();
			driver.findElement(By.id("Email")).clear();
			driver.findElement(By.id("Email")).sendKeys(username);
			driver.findElement(By.id("next")).click();
			driver.findElement(By.id("Passwd")).clear();
			driver.findElement(By.id("Passwd")).sendKeys(password);
			driver.findElement(By.id("signIn")).click();
			driver.findElement(By.id("searchboxinput")).clear();
			driver.findElement(By.id("searchboxinput")).sendKeys(place.getAddress());
			driver.findElement(By.cssSelector("button.searchbox-searchbutton")).click();
			driver.findElement(By.cssSelector("button.widget-pane-term-link.widget-pane-link")).click();
			driver.findElement(By.cssSelector("input.rap-text-input")).clear();
			driver.findElement(By.cssSelector("input.rap-text-input")).sendKeys(place.getName());

			driver.findElement(By.cssSelector(
					"div.add-a-place-card-field.add-a-place-card-field-type-website > div.issue-card-field-value-container > div.rap-text-input-container > input.rap-text-input"))
					.clear();
			driver.findElement(By.cssSelector(
					"div.add-a-place-card-field.add-a-place-card-field-type-website > div.issue-card-field-value-container > div.rap-text-input-container > input.rap-text-input"))
					.sendKeys(place.getWebsite());

			driver.findElement(By.cssSelector(
					"div.add-a-place-card-field.add-a-place-card-field-type-category > div.issue-card-field-value-container > div.rap-text-input-container > input.rap-text-input"))
					.clear();
			driver.findElement(By.cssSelector(
					"div.add-a-place-card-field.add-a-place-card-field-type-category > div.issue-card-field-value-container > div.rap-text-input-container > input.rap-text-input"))
					.sendKeys("Unterkü");

			driver.findElement(By.cssSelector(
					"div.add-a-place-card-field.add-a-place-card-field-type-category > div.issue-card-field-value-container > div.rap-text-input-container > input.rap-text-input")).sendKeys(Keys.TAB);

			//driver.findElement(By.xpath("//div[@id='rap-card']/div/div/div[2]/div[3]/button")).click();

			placeAddResponse.setPlaceId("NO_PLACE_ID_ADDED_WITH_SELENIUM_" + username);
			placeAddResponse.setStatus("OK");
			return placeAddResponse;
		} catch (Exception e) {
			System.err.println("error " + e);
			placeAddResponse.setStatus("FAILED");
			return placeAddResponse;
		} finally{
			driver.close();
		}
	}

}
