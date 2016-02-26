package com.tripagor.importer;

import java.net.URL;
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

public class BookingComAccommodationExtractor {

	private WebDriver driver;
	private final Logger logger = LoggerFactory.getLogger(BookingComAccommodationExtractor.class);
	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private GeoApiContext context;
	private AddressComponent[] addressComponents;

	public BookingComAccommodationExtractor() {
		context = new GeoApiContext().setApiKey(API_KEY);
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
			result.setAddress(getAddress(normalizeAddressLine(address.getText())));

			return result;
		} catch (Exception e) {
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
			addressComponents = results[0].addressComponents;
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
						address.setStreet(addressComponent.longName);
						break;
					} else if (addressComponentType == AddressComponentType.ROUTE) {
						address.setStreet(addressComponent.longName);
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
