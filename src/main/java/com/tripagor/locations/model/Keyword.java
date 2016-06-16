package com.tripagor.locations.model;

public class Keyword {

	private String value;
	private Number competition;
	private Number monthlySearches;
	private String currencyCode;
	private Number suggestedBid;

	public Number getCompetition() {
		return competition;
	}

	public void setCompetition(Number competition) {
		this.competition = competition;
	}

	public Number getMonthlySearches() {
		return monthlySearches;
	}

	public void setMonthlySearches(Number monthlySearches) {
		this.monthlySearches = monthlySearches;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Number getSuggestedBid() {
		return suggestedBid;
	}

	public void setSuggestedBid(Number suggestedBid) {
		this.suggestedBid = suggestedBid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
