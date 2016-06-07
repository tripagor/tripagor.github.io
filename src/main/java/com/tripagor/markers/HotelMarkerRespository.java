package com.tripagor.markers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tripagor.markers.model.HotelMarker;
import com.tripagor.markers.model.Scope;

@RepositoryRestResource
public interface HotelMarkerRespository extends MongoRepository<HotelMarker, String> {

	Page<HotelMarker> findByIsOwnedAndScope(boolean isOwned, Scope scope, Pageable pageable);

	HotelMarker findByReference(String reference);

}
