package org.search.dao;

import org.search.dto.Continent;
import org.search.dto.Country;
import org.search.dto.Flags;

public interface FlagSearchDao {
	
	public Country getCountry(String name);

	public Continent getContinent(String continentname);
	
	public Flags getFlags();

	public boolean addContinents(Flags f);
	
}
