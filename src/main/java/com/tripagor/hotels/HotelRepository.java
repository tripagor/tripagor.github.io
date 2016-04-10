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

	Hotel findByBookingComId(@Param("bookingId") Long id);

	long countByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
			@Param("isEvaluated") boolean isEvaluated, @Param("isMarkerSet") boolean isMarkerSet,
			@Param("isMarkerApproved") boolean isMarkerApproved,
			@Param("isFormattedAddressExisting") boolean formattedAddressExists,
			@Param("isPlaceIdExisting") boolean isPlaceIdExisting);

	long countByIsEvaluatedExists(@Param("isEvaluatedExisting") boolean isEvaluated);

	Page<List<Hotel>> findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
			@Param("isEvaluated") boolean isEvaluated, @Param("isMarkerSet") boolean isMarkerSet,
			@Param("isMarkerApproved") boolean isMarkerApproved,
			@Param("isFormattedAddressExisting") boolean formattedAddressExists,
			@Param("isPlaceIdExisting") boolean isPlaceIdExisting, Pageable pageable);

	Page<List<Hotel>> findByIsEvaluatedExists(@Param("isEvaluatedExisting") boolean isEvaluatedExisting,
			Pageable pageable);

	long countBy();

}
