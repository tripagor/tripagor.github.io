package com.tripagor.hotels.model;

public enum WorldRegion {
	NORTH_AMERICA(1), MIDDLE_AMERICA(2), SOUTH_AMERICA(3), OTHER(4), AFRICA(5), EUROPE(6), MIDDLE_EAST(7), ASIA(
			8), AUSTRALIA_OCEANIA(9), CARIBBEAN(10), UNKNOWN(0);
	private int continentId;

	WorldRegion(int continentId) {
		this.continentId = continentId;
	}

	public int toValue() {
		return this.continentId;
	}

	@Override
	public String toString() {
		return ""+continentId;
	}

}
