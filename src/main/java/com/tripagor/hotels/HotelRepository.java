package com.tripagor.hotels;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tripagor.hotels.model.Hotel;

@RepositoryRestResource
public interface HotelRepository extends MongoRepository<Hotel, String> {

	Hotel findByBookingComId(@Param("bookingId") Long id);

	Page<Hotel> findByContinentId(Integer continentId, Pageable pageable);

	Page<Hotel> findByCountryCode(String countryCode, Pageable pageable);

	long countBy();

	Page<Hotel> findAll(Sort sort, Pageable pageRequest);

}
