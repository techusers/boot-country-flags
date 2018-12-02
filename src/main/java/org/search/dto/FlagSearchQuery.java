package org.search.dto;

import java.util.ArrayList;

public class FlagSearchQuery {

	private ArrayList<Continent> continents = new ArrayList<Continent>();
	
	private ArrayList<Country> countries = new ArrayList<Country>();

	public ArrayList<Continent> getContinents() {
		return continents;
	}

	public void setContinents(ArrayList<Continent> continents) {
		this.continents = continents;
	}

	public ArrayList<Country> getCountries() {
		return countries;
	}

	public void setCountries(ArrayList<Country> countries) {
		this.countries = countries;
	}
	
}
