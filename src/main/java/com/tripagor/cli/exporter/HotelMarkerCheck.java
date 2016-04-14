package com.tripagor.cli.exporter;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceIdScope;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.model.Hotel;

@Component
public class HotelMarkerCheck {

	private HotelService hotelService;
	private GeoApiContext geoApiContext;
	private String postfix;
	private Logger logger = LoggerFactory.getLogger(HotelMarkerCheck.class);

	@Autowired
	public HotelMarkerCheck(HotelService hotelService, GeoApiContext geoApiContext,
			@Value("${hotel.url.postfix}") String postfix) {
		this.hotelService = hotelService;
		this.geoApiContext = geoApiContext;
		if (postfix == null) {
			this.postfix = "";
		} else {
			this.postfix = postfix;
		}
	}

	public void doCheck() {
		int currentPage = 0;
		long totalPages = 1;
		int pageSize = 50;

		while (currentPage < totalPages) {
			Page<Hotel> pagedResources = hotelService
					.findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
							currentPage++, pageSize, true, true, false, true, true);
			totalPages = pagedResources.getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
				try {
					LatLng hotelLatLng = new LatLng(Double.parseDouble(hotel.getLatitude()),
							Double.parseDouble(hotel.getLongitude()));
					PlacesSearchResponse response = PlacesApi.nearbySearchQuery(geoApiContext, hotelLatLng)
							.rankby(RankBy.DISTANCE).type(PlaceType.LODGING).await();
					for (PlacesSearchResult result : response.results) {
						if (result.name.equals(hotel.getName()) && result.scope == PlaceIdScope.GOOGLE) {
							PlaceDetails placeDetails = PlacesApi.placeDetails(geoApiContext, result.placeId).await();

							if (hotel.getUrl().concat(postfix).equals(placeDetails.website.toString())) {
								logger.debug(hotel.getName() + " APPROVED " + result.scope + " " + result.placeId);
								hotel.setIsMarkerApproved(true);
								hotel.setPlaceId(placeDetails.placeId);
								hotelService.update(hotel);
							} else {
								logger.debug(
										hotel.getName() + " NOT APPROVED " + result.scope + " " + result.placeId);
								hotel.setIsMarkerApproved(false);
								hotel.setPlaceId(null);
								hotelService.update(hotel);
							}
							break;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
