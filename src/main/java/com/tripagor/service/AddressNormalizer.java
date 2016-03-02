package com.tripagor.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.tripagor.importer.model.Address;

public class AddressNormalizer {

	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";

	private GeoApiContext context;

	public AddressNormalizer() {
		context = new GeoApiContext().setApiKey(API_KEY);
	}

	public Address normalize(String addressLine) throws Exception {
		Address address = new Address();
		GeocodingResult[] results = GeocodingApi.geocode(context, addressLine).await();
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

	public String normalizedString(String addressLine) throws Exception {
		GeocodingResult[] results = GeocodingApi.geocode(context, addressLine).await();
		if (results.length > 0) {
			return results[0].formattedAddress;
		} else {
			throw new RuntimeException("no address data found");
		}
	}

	public String wellFormattedString(double latitude, double longitude) throws Exception {
		GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(latitude, longitude)).await();
		if (results.length > 0) {
			return results[0].formattedAddress;
		} else {
			throw new RuntimeException("no address data found");
		}
	}

}
