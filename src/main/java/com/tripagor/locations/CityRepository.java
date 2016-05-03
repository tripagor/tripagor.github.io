package com.tripagor.locations;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {

	City findByNameAndCountryCode(String name, String countryCode);

	List<City> findByCountryCode(String countryCode);

}
