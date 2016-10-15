package com.tripagor.hotels.model;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WorldRegion {
	NORTH_AMERICA(1), MIDDLE_AMERICA(2), SOUTH_AMERICA(3), OTHER(4), AFRICA(5), EUROPE(6), MIDDLE_EAST(7), ASIA(
			8), AUSTRALIA_OCEANIA(9), CARIBBEAN(10), UNKNOWN(0);
	private int continentId;
	private static Map<Integer, WorldRegion> map = Stream.of(WorldRegion.values())
			.collect(Collectors.toMap(s -> s.toValue(), Function.identity()));

	WorldRegion(int continentId) {
		this.continentId = continentId;
	}

	public int toValue() {
		return this.continentId;
	}

	public static WorldRegion fromValue(Integer value) {
		WorldRegion region = map.get(value);
		if (region == null) {
			return map.get(0);
		} else {
			return region;
		}
	}


}
