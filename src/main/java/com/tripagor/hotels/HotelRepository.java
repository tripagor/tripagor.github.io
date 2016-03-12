package com.tripagor.hotels;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tripagor.hotels.model.Hotel;

@RepositoryRestResource
public interface HotelRepository extends MongoRepository<Hotel, String> {

	Hotel findByBookingComId(@Param("bookingId") long id);

	Page<List<Hotel>> findByIsEvaluatedExists(@Param("isExisting") boolean isExisting, Pageable pageable);

	long countByIsEvaluatedExists(@Param("isExisting") boolean isExisting);

	Page<List<Hotel>> findByIsEvaluated(@Param("isEvaluated") boolean isEvaluted, Pageable pageable);

	long countByIsEvaluated(@Param("isEvaluated") boolean isEvaluted);

	Page<List<Hotel>> findByFormattedAddressExistsAndIsMarkerSet(@Param("isExisting") boolean isExisting,
			@Param("isMarkerSet") boolean isMarkerSet, Pageable pageable);

	long countByFormattedAddressExistsAndIsMarkerSet(@Param("isExisting") boolean isExisting,
			@Param("isMarkerSet") boolean isMarkerSet);
	long countBy();


}
