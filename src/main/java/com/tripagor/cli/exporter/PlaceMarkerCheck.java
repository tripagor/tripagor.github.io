package com.tripagor.cli.exporter;

import java.util.Collection;

import org.springframework.hateoas.PagedResources;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceIdScope;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.tripagor.cli.service.DistanceCalculator;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.model.Hotel;

public class PlaceMarkerCheck {

	private HotelService hotelService;
	private GeoApiContext geoApiContext;
	private DistanceCalculator distanceCalculator;

	public PlaceMarkerCheck(HotelService hotelService, GeoApiContext geoApiContext) {
		this.hotelService = hotelService;
		this.geoApiContext = geoApiContext;
		this.distanceCalculator = new DistanceCalculator();
	}

	public void doCheck() {
		int currentPage = 0;
		long totalPages = 1;
		int pageSize = 50;

		while (currentPage < totalPages) {
			PagedResources<Hotel> pagedResources = hotelService
					.findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists(currentPage++,
							pageSize, true, true, false, true);
			totalPages = pagedResources.getMetadata().getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
				try {
					LatLng hotelLatLng = new LatLng(Double.parseDouble(hotel.getLatitude()),
							Double.parseDouble(hotel.getLongitude()));
					PlacesSearchResponse response = PlacesApi.nearbySearchQuery(geoApiContext, hotelLatLng)
							.rankby(RankBy.DISTANCE).type(PlaceType.LODGING).await();
					for (PlacesSearchResult result : response.results) {
						if (result.name.equals(hotel.getName()) && result.scope == PlaceIdScope.GOOGLE) {
							System.out.println(
									hotel.getName() + " seems to be approved " + result.scope + " " + result.placeId);
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
