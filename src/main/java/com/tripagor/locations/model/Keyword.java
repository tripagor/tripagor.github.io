package com.tripagor.locations.model;

public class Keyword {

	private String name;
	private double competition;
	private long monthlySearches;
	private String currencyCode;
	private double suggestedBid;

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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getSuggestedBid() {
		return suggestedBid;
	}

	public void setSuggestedBid(double suggestedBid) {
		this.suggestedBid = suggestedBid;
	}

	public long getMonthlySearches() {
		return monthlySearches;
	}

	public void setMonthlySearches(long monthlySearches) {
		this.monthlySearches = monthlySearches;
	}
}
