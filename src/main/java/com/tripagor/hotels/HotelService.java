package com.tripagor.hotels;

import org.springframework.hateoas.PagedResources;

import com.tripagor.hotels.model.Hotel;

public interface HotelService {

	Hotel getByBookingComId(Long bookingComId);

	Hotel create(Hotel hotel);

	Hotel update(Hotel hotel);

	PagedResources<Hotel> findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
			int currentPage, int pageSize, boolean isEvaluated, boolean isMarkerSet, boolean isMarkerApproved,
			boolean isFormettedAddressExisting, boolean isPlaceIdExisting);

	PagedResources<Hotel> findByIsEvaluatedExists(int i, Object pageSize, boolean b);

}
