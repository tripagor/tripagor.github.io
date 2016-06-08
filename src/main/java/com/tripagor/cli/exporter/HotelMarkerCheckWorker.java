package com.tripagor.cli.exporter;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.tripagor.cli.service.DistanceCalculator;
import com.tripagor.cli.service.StringSimilarity;
import com.tripagor.markers.HotelMarkerRespository;
import com.tripagor.markers.model.HotelMarker;
import com.tripagor.markers.model.Scope;

@Component
public class HotelMarkerCheckWorker {

	private GeoApiContext geoApiContext;
	private String postfix;
	private Logger logger = LoggerFactory.getLogger(HotelMarkerCheckWorker.class);
	private StringSimilarity stringSimilarity;
	private DistanceCalculator distanceCalculator;
	private HotelMarkerRespository hotelMarkerRespository;
	private static final long ACCURACY = 20;

	@Autowired
	public HotelMarkerCheckWorker(HotelMarkerRespository hotelMarkerRespository, GeoApiContext geoApiContext,
			@Value("${hotel.url.postfix}") String postfix) {
		stringSimilarity = new StringSimilarity();
		distanceCalculator = new DistanceCalculator();
		this.hotelMarkerRespository = hotelMarkerRespository;
		this.geoApiContext = geoApiContext;
		if (postfix == null) {
			this.postfix = "";
		} else {
			this.postfix = postfix;
		}
	}

	public Collection<HotelMarker> doCheck() {
		Collection<HotelMarker> changedMarkerStatusHotels = new LinkedList<>();

		int currentPage = 0;
		long totalPages = 1;
		int pageSize = 50;

		while (currentPage < totalPages) {
			Page<HotelMarker> pagedResources = hotelMarkerRespository.findByIsOwnedAndScope(true, Scope.APP,
					new PageRequest(currentPage, pageSize));
			totalPages = pagedResources.getTotalPages();
			Collection<HotelMarker> hotelMarkers = pagedResources.getContent();

			for (HotelMarker hotelMarker : hotelMarkers) {
				try {
					LatLng hotelLatLng = new LatLng(hotelMarker.getLocation().getLat(),
							hotelMarker.getLocation().getLng());
					PlacesSearchResponse response = PlacesApi.nearbySearchQuery(geoApiContext, hotelLatLng)
							.rankby(RankBy.DISTANCE).type(PlaceType.LODGING).await();
					for (PlacesSearchResult result : response.results) {
						float cosineDistance = stringSimilarity.cosineDistance(hotelMarker.getName(), result.name);
						float jaroDistance = stringSimilarity.jaroDistance(hotelMarker.getName(), result.name);
						float geometricalDistance = distanceCalculator.distance(hotelLatLng, result.geometry.location);

						if ((cosineDistance >= 0.5 || jaroDistance >= 0.8) && geometricalDistance <= ACCURACY) {
							PlaceDetails placeDetails = PlacesApi.placeDetails(geoApiContext, result.placeId).await();
							if (result.scope == PlaceIdScope.GOOGLE) {
								if (placeDetails.website != null && hotelMarker.getWebsite().concat(postfix)
										.equals(placeDetails.website.toString())) {
									logger.debug(
											hotelMarker.getName() + " APPROVED " + result.scope + " " + result.placeId);
									hotelMarker.setIsOwned(true);
								} else {
									logger.debug(hotelMarker.getName() + " NOT APPROVED " + result.scope + " "
											+ result.placeId);
									hotelMarker.setIsOwned(false);
								}
								hotelMarker.setScope(Scope.GOOGLE);
								hotelMarker.setPlaceId(placeDetails.placeId);
								changedMarkerStatusHotels.add(hotelMarker);
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			currentPage++;
		}

		for (HotelMarker changed : changedMarkerStatusHotels) {
			hotelMarkerRespository.save(changed);
		}

		return changedMarkerStatusHotels;
	}
}
