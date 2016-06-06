package com.tripagor.hotels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.tripagor.hotels.model.Hotel;

@Component
public class HotelServicePersistenceImpl implements HotelService {

	private HotelRepository hotelRepository;

	@Autowired
	public HotelServicePersistenceImpl(HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
	}

	@Override
	public Hotel getByBookingComId(Long bookingComId) {
		return hotelRepository.findByBookingComId(bookingComId);
	}

	@Override
	public Hotel createOrModify(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	@Override
	public Page<Hotel> findNewest(int currentPage, int pageSize) {
		return hotelRepository.findAll(new Sort(Sort.Direction.ASC, "bookingComId"), new PageRequest(currentPage, pageSize));
	}

	@Override
	public void createOrModify(Iterable<Hotel> hotels) {
		hotelRepository.save(hotels);
	}

}
