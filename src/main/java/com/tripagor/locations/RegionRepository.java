package com.tripagor.locations;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tripagor.locations.model.City;
import com.tripagor.locations.model.Region;

public interface RegionRepository extends MongoRepository<Region, String> {
	
	Region findByNameAndCountryCode(String name, String countryCode);

	List<City> findByCountryCode(String string);

}
