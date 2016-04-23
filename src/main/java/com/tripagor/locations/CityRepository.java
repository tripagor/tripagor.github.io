package com.tripagor.locations;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {

	City findByNameAndCountryCode(String name, String countryCode);

}
