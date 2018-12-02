package org.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Country extends BaseDto {

	private String name;
	
	private String flag;
	
	//add json ignorable
	private int countryid;
	
	private int continentid;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@JsonIgnore
	public int getCountryid() {
		return countryid;
	}

	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}

	@JsonIgnore
	public int getContinentid() {
		return continentid;
	}

	public void setContinentid(int continentid) {
		this.continentid = continentid;
	}
	
}
