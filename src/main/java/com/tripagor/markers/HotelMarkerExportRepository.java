package com.tripagor.markers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tripagor.markers.model.HotelMarkerExport;

@RepositoryRestResource
public interface HotelMarkerExportRepository extends MongoRepository<HotelMarkerExport, String> {

}
