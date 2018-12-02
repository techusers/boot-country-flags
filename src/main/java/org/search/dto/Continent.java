package org.search.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Continent  extends BaseDto {

	private String continent;
	
	private ArrayList<Country> countries = new ArrayList<Country>();
	
	private int continentid;

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public ArrayList<Country> getCountries() {
		return countries;
	}

	public void setCountries(ArrayList<Country> countries) {
		this.countries = countries;
	}

	@JsonIgnore
	public int getContinentid() {
		return continentid;
	}

	public void setContinentid(int continentid) {
		this.continentid = continentid;
	}
	
}
