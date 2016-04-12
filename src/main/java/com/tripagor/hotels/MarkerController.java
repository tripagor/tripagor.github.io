package com.tripagor.hotels;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tripagor.cli.exporter.PlaceMarker;
import com.tripagor.hotels.model.Hotel;

@RestController(value = "/api/hotels")
public class MarkerController {
	private PlaceMarker placeMarker;
	private String appendString;

	@Autowired
	public MarkerController(PlaceMarker placeMarker, String appendString) {
		super();
		this.placeMarker = placeMarker;
		this.appendString = appendString;
	}

	@RequestMapping(method = RequestMethod.POST)
	public Collection<Hotel> mark() {
		return placeMarker.doMark(50, this.appendString);
	}

}
