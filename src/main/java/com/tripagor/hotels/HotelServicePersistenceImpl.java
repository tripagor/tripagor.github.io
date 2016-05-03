package com.tripagor.hotels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	public Hotel create(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	@Override
	public Hotel update(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	@Override
	public Page<Hotel> findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
			int currentPage, int pageSize, boolean isEvaluated, boolean isMarkerSet, boolean isMarkerApproved,
			boolean isFormettedAddressExisting, boolean isPlaceIdExisting) {
		return hotelRepository
				.findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
						isEvaluated, isMarkerSet, isMarkerApproved, isFormettedAddressExisting, isPlaceIdExisting,
						new PageRequest(currentPage, pageSize));
	}

	@Override
	public Page<Hotel> findByIsEvaluatedExists(int currentPage, int pageSize, boolean isEvaluatedExisting) {
		return hotelRepository.findByIsEvaluatedExistsOrderByBookingComIdAsc(isEvaluatedExisting,
				new PageRequest(currentPage, pageSize));
	}

}
