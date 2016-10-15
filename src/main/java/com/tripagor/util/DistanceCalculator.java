package com.tripagor.util;

import com.google.maps.model.LatLng;

public class DistanceCalculator {

	public float distance(LatLng start, LatLng end) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(end.lat - start.lat);
		double dLng = Math.toRadians(end.lng - start.lng);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(start.lat))
				* Math.cos(Math.toRadians(end.lat)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

}
