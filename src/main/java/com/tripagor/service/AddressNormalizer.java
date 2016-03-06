package com.tripagor.service;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.tripagor.importer.model.Address;
import com.tripagor.importer.model.Result;
import com.tripagor.importer.model.ReverseGeocodingResult;

public class AddressNormalizer {

	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private final String GOOGLE_API_GEOCODING_REVERSE_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
	private RestTemplate restTemplate;
	private String apiKey;

	private GeoApiContext context;

	public AddressNormalizer() {
		apiKey = API_KEY;
		context = new GeoApiContext().setApiKey(apiKey);
		restTemplate = new RestTemplate();
	}

	public Address normalize(String addressLine) throws Exception {
		GeocodingResult[] results = GeocodingApi.geocode(context, addressLine).await();
		if (results.length > 0) {
			return this.getAddress(results[0].addressComponents);
		} else {
			throw new RuntimeException("no address data found");
		}
	}

	public Address getAdress(List<com.tripagor.importer.model.AddressComponent> components) {

		Address address = new Address();
		for (com.tripagor.importer.model.AddressComponent addressComponent : components) {
			List<String> types = addressComponent.getTypes();
			for (String addressComponentType : types) {
				if ("country".equals(addressComponentType)) {
					address.setCountry(addressComponent.getLongName());
					break;
				} else if ("postal_code".equals(addressComponentType)) {
					address.setPostalCode(addressComponent.getLongName());
					break;
				} else if ("street_number".equals(addressComponentType)) {
					address.setStreetNumber(addressComponent.getLongName());
					break;
				} else if ("locality".equals(addressComponentType)) {
					address.setCity(addressComponent.getLongName());
					break;
				} else if ("street_address".equals(addressComponentType)) {
					address.setStreetName(addressComponent.getLongName());
					break;
				} else if ("route".equals(addressComponentType)) {
					address.setStreetName(addressComponent.getLongName());
					break;
				}
			}
		}

		return address;

	}

	public Address getAddress(AddressComponent[] components) {
		Address address = new Address();
		for (AddressComponent addressComponent : components) {
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

		return address;

	}

	public String normalizedString(String addressLine) throws Exception {
		GeocodingResult[] results = GeocodingApi.geocode(context, addressLine).await();
		if (results.length > 0) {
			return results[0].formattedAddress;
		} else {
			throw new RuntimeException("no address data found");
		}
	}

	public List<Result> reverseGeocoding(double latitude, double longitude) {
		return restTemplate
				.getForObject(GOOGLE_API_GEOCODING_REVERSE_BASE_URL + latitude + "," + longitude + "&key=" + apiKey,
						ReverseGeocodingResult.class)
				.getResults();
	}

	public String wellFormattedString(double latitude, double longitude) throws Exception {
		List<Result> results = this.reverseGeocoding(latitude, longitude);
		if (results.size() > 0) {
			return new String(results.get(0).getFormattedAddress().getBytes(), "UTF-8");
		} else {
			throw new RuntimeException("no address data found");
		}
	}

}
