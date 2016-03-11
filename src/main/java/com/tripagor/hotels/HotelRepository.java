package com.tripagor.hotels;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tripagor.hotels.model.Hotel;

public interface HotelRepository extends MongoRepository<Hotel, String> {

}
