package com.tripagor.hotels;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping(consumes = "application/json", path = "markers", method = RequestMethod.POST)
	public Collection<Hotel> mark(@RequestBody Map<String, Object> valueMap) {
		Integer numberOfPlacesToAdd = (Integer) valueMap.get("numberOfPlacesToAdd");
		return placeMarker.doMark(numberOfPlacesToAdd, this.appendString);
	}

}
