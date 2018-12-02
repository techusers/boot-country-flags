package org.search.service;

import org.search.dto.Continent;
import org.search.dto.Country;
import org.search.dto.EtlRequestDto;

public interface FlagSearchService {
	
	Country getCountry(String countryname);

	Continent getContinent(String continentname);

	void triggerETL (EtlRequestDto input);
}
