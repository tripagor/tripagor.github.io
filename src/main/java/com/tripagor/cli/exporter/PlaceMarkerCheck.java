package com.tripagor.cli.exporter;

import java.util.Collection;

import org.springframework.hateoas.PagedResources;

import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.model.Hotel;

public class PlaceMarkerCheck {

	private HotelService hotelService;


	public PlaceMarkerCheck(HotelService hotelService) {
		this.hotelService = hotelService;
	}

	public void doCheck() {

		int currentPage = 0;
		long totalPages = 1;
		int pageSize= 50;

		while (currentPage < totalPages) {
			PagedResources<Hotel> pagedResources = hotelService
					.findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists(currentPage++,
							pageSize, true, true, false, true);
			totalPages = pagedResources.getMetadata().getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
				
			}
		}
	}
}
