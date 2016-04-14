package com.tripagor.markers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tripagor.markers.model.HotelMarkerCheck;

@RepositoryRestResource
public interface HotelMarkerCheckRepository extends MongoRepository<HotelMarkerCheck, String> {

}
