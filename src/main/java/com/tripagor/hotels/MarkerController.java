package com.tripagor.hotels;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripagor.cli.exporter.PlaceMarker;
import com.tripagor.hotels.model.Hotel;

@Controller
public class MarkerController {
	private PlaceMarker placeMarker;
	private String appendString;

	@Autowired
	public MarkerController(PlaceMarker placeMarker, @Value("${hotel.url.postfix}") String appendString) {
		super();
		this.placeMarker = placeMarker;
		this.appendString = appendString;
	}

	@RequestMapping(path = "markers", method = RequestMethod.POST)
	public Collection<Hotel> mark() {
		return placeMarker.doMark(50, this.appendString);
	}

}
