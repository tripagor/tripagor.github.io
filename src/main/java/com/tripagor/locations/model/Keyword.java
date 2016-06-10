package com.tripagor.locations.model;

public class Keyword {

	private String name;
	private double competition;
	private double monthlySearches;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCompetition() {
		return competition;
	}

	public void setCompetition(double competition) {
		this.competition = competition;
	}

	public double getMonthlySearches() {
		return monthlySearches;
	}

	public void setMonthlySearches(double monthlySearches) {
		this.monthlySearches = monthlySearches;
	}
}
