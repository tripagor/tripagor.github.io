package com.tripagor.hotels;

import org.springframework.data.domain.Page;

import com.tripagor.hotels.model.Hotel;

public interface HotelService {

	Hotel getByBookingComId(Long bookingComId);

	Hotel createOrModify(Hotel hotel);

	Page<Hotel> findNewest(int currentPage, int pageSize);

	void createOrModify(Iterable<Hotel> hotels);
}
