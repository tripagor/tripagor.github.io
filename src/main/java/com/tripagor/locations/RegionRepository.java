package com.tripagor.locations;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegionRepository extends MongoRepository<Region, String> {
	
	Region findByNameAndCountryCode(String name, String countryCode);

}
