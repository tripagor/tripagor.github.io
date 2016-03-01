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

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.tripagor.importer.model.Accommodation;
import com.tripagor.importer.model.Address;
import com.tripagor.importer.model.FeatureSection;

public class BookingComAccommodationExtractor {

	private final Logger logger = LoggerFactory.getLogger(BookingComAccommodationExtractor.class);
	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private GeoApiContext context;

	public BookingComAccommodationExtractor() {
		context = new GeoApiContext().setApiKey(API_KEY);
	}

	public BookingComAccommodationExtractor(String apiKey) {
		context = new GeoApiContext().setApiKey(apiKey);
	}

	public Accommodation extract(String url) throws RuntimeException {
		try {
			WebDriver driver = new FirefoxDriver();
			Accommodation result = new Accommodation();

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
			result.setAddress(getAddress(normalizeAddressLine(addressLine)));

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

	private Address getAddress(String normalizedAddressLine) throws Exception {
		Address address = new Address();
		GeocodingResult[] results = GeocodingApi.geocode(context, normalizedAddressLine).await();
		if (results.length > 0) {
			AddressComponent[] addressComponents = results[0].addressComponents;
			address.setLatitude(results[0].geometry.location.lat);
			address.setLongitude(results[0].geometry.location.lng);
			for (AddressComponent addressComponent : addressComponents) {
				AddressComponentType[] types = addressComponent.types;
				for (AddressComponentType addressComponentType : types) {
					if (addressComponentType == AddressComponentType.COUNTRY) {
						address.setCountry(addressComponent.longName);
						break;
					} else if (addressComponentType == AddressComponentType.POSTAL_CODE) {
						address.setPostalCode(addressComponent.longName);
						break;
					} else if (addressComponentType == AddressComponentType.STREET_NUMBER) {
						address.setStreetNumber(addressComponent.longName);
						break;
					} else if (addressComponentType == AddressComponentType.LOCALITY) {
						address.setCity(addressComponent.longName);
						break;
					} else if (addressComponentType == AddressComponentType.STREET_ADDRESS) {
						address.setStreetName(addressComponent.longName);
						break;
					} else if (addressComponentType == AddressComponentType.ROUTE) {
						address.setStreetName(addressComponent.longName);
						break;
					}
				}
			}
		} else {
			throw new RuntimeException("no address data found");
		}
		return address;
	}
}
