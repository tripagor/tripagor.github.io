package com.tripagor.importer;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripagor.importer.model.FeatureSection;
import com.tripagor.importer.model.Lodging;
import com.tripagor.service.AddressNormalizer;

public class BookingComAccommodationExtractor {

	private final Logger logger = LoggerFactory.getLogger(BookingComAccommodationExtractor.class);
	private AddressNormalizer addressNormalizer;

	public BookingComAccommodationExtractor() {
		addressNormalizer = new AddressNormalizer();
	}

	public Lodging extract(String url) {
		try {
			WebDriver driver = new FirefoxDriver();
			Lodging result = new Lodging();

			driver.manage().timeouts().pageLoadTimeout(20000, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			URL urlObj = new URL(url.replace("http:/", "http://"));
			driver.get(urlObj.toString());
			String addressLine = driver.findElement(By.className("hp_address_subtitle")).getText();
			String desc = driver.findElement(By.id("summary")).getText();
			String title = driver.findElement(By.id("hp_hotel_name")).getText();

			List<WebElement> featureSections = driver
					.findElements(By.xpath("//div[@class='facilitiesChecklistSection']"));
			for (WebElement featureSectionElement : featureSections) {
				FeatureSection featureSection = new FeatureSection();
				featureSection.setName(featureSectionElement.findElement(By.tagName("h5")).getText());
				result.getFeatureSections().add(featureSection);

				List<WebElement> featureElements = featureSectionElement.findElements(By.tagName("li"));
				for (WebElement featureElement : featureElements) {
					featureSection.getFeatures().add(featureElement.getText());
				}
			}

			List<WebElement> images = driver.findElements(By.xpath("//div[@class='slick-slide']//img"));
			for (WebElement image : images) {
				String src = image.getAttribute("src");
				String dataLazy = image.getAttribute("data-lazy");
				if (src != null) {
					result.getImageUrls().add(src);
				} else if (dataLazy != null) {
					result.getImageUrls().add(dataLazy);
				}
			}

			result.setUrl(url);
			result.setDescription(desc);
			result.setName(title);
			result.setAddress(addressNormalizer.normalize(normalizeAddressLine(addressLine)));

			driver.close();

			return result;
		} catch (Exception e) {
			logger.error("failed with{}", e);
			throw new RuntimeException(e);
		}
	}

	private String normalizeAddressLine(String origin) {

		try {
			String normalizedLine = "";
			String[] addressParts = origin.split(",");
			if (addressParts.length == 3) {
				normalizedLine = addressParts[0].trim().concat(", ").concat(addressParts[1].trim()).concat(", ")
						.concat(addressParts[2].trim());
			} else if (addressParts.length == 4) {
				normalizedLine = addressParts[0].trim().concat(", ").concat(addressParts[2].trim()).concat(", ")
						.concat(addressParts[3].trim());
			}

			return normalizedLine;
		} catch (Exception e) {
			throw new RuntimeException(new IllegalArgumentException("addressLine is invalid"));
		}
	}

}
