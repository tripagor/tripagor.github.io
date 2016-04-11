package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.tripagor.cli.service.PlaceAddApi;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.model.Location;
import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;

public class PlaceMarker {

	private int numberOfPlacesToAdd;
	private String appendStr = "";
	private int pageSize = 50;
	private HotelService hotelService;
	private PlaceAddApi placeAddApi;

	@Autowired
	public PlaceMarker(HotelService hotelService, PlaceAddApi placeAddApi) {
		this.hotelService = hotelService;
		this.placeAddApi = placeAddApi;
	}

	public void doMark() {

		boolean isMaxiumimNumber = false;
		int currentNumberAdded = 0;

		int currentPage = 0;
		long totalPages = 1;

		while (currentPage < totalPages && !isMaxiumimNumber) {
			Page<Hotel> pagedResources = hotelService
					.findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
							currentPage++, pageSize, true, false, false, true, false);
			totalPages = pagedResources.getTotalPages();
			List<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
				if (numberOfPlacesToAdd != 0 && currentNumberAdded >= numberOfPlacesToAdd) {
					isMaxiumimNumber = true;
					break;
				}
				try {
					System.out.println(hotel.getBookingComId() + " " + hotel.getName());
					PlaceAddRequest place = new PlaceAddRequest();

					place.setName(hotel.getName());
					place.setAddress(hotel.getFormattedAddress());
					Location location = new Location();
					location.setLat(new BigDecimal(hotel.getLatitude()).doubleValue());
					location.setLng(new BigDecimal(hotel.getLongitude()).doubleValue());
					place.setLocation(location);
					place.setAccuracy(30);
					place.setWebsite(hotel.getUrl() + appendStr);
					place.setTypes(Arrays.asList(new String[] { "lodging" }));

					PlaceAddResponse response = placeAddApi.add(place);
					if ("OK".equals(response.getStatus())) {
						hotel.setPlaceId(response.getPlaceId());
						hotel.setIsMarkerSet(true);

						hotelService.update(hotel);
						currentNumberAdded++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setNumberOfPlacesToAdd(int numberOfPlacesToAdd) {
		this.numberOfPlacesToAdd = numberOfPlacesToAdd;
	}

	public void setAppendStr(String appendStr) {
		this.appendStr = appendStr;
	}

}
