package com.tripagor.hotels;

import org.springframework.data.domain.Page;

import com.tripagor.hotels.model.Hotel;

public interface HotelService {

	Hotel getByBookingComId(Long bookingComId);

	Hotel createOrModify(Hotel hotel);

	Page<Hotel> findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
			int currentPage, int pageSize, boolean isEvaluated, boolean isMarkerSet, boolean isMarkerApproved,
			boolean isFormettedAddressExisting, boolean isPlaceIdExisting);

	Page<Hotel> findByIsEvaluatedExists(int currentPage, int pageSize, boolean isEvaluatedExisting);

	void createOrModify(Iterable<Hotel> hotels);
}
