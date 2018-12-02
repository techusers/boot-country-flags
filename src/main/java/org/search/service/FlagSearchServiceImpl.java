package org.search.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.search.dao.FlagSearchDao;
import org.search.dto.Continent;
import org.search.dto.Country;
import org.search.dto.EtlRequestDto;
import org.search.dto.Flags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Controller
public class FlagSearchServiceImpl implements FlagSearchService {
	
	Logger logger = LoggerFactory.getLogger(FlagSearchServiceImpl.class);
			
	@Autowired
	private FlagSearchDao searchDao;
	
	@Autowired
	private FlagSearchMetrics flagMetrics;
	
	private final MeterRegistry registry;
	
	public FlagSearchServiceImpl(MeterRegistry registry) {
		this.registry = registry;
	}
	
	//Get flag by country name
	@GetMapping("/flag/country/{countryname}")
	@ResponseBody
	public Country getCountry(@PathVariable String countryname) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Retrieving flag for country : "+countryname);
		}
		
		Timer timer = registry.timer("flag.country.timer");
		Long start = System.currentTimeMillis();
		
		flagMetrics.incrementCountry(countryname);
		Country c = searchDao.getCountry(countryname);
		
		timer.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
		
		return c;
	}
	
	//Get flag by continent name
	@GetMapping("/flag/continent/{continentname}")
	@ResponseBody
	public Continent getContinent(@PathVariable String continentname) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Retrieving flag for continent : "+continentname);
		}
		
		Timer timer = registry.timer("flag.continent.timer");
		Long start = System.currentTimeMillis();
		
		flagMetrics.incrementContinent(continentname);
		Continent continent = searchDao.getContinent(continentname);
		
		timer.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
		
		return continent;
	}
	
	//get all flags. all continents and countries flag will be retrieved
	@GetMapping("/flag")
	@ResponseBody
	public ArrayList<Continent> getFlag() {
		
		logger.info("Retrieving flags for all Continents ");
		
		Timer timer = registry.timer("flag.all.timer");
		Long start = System.currentTimeMillis();
		
		flagMetrics.incrementContinent("AllFlags");
		Flags flags = searchDao.getFlags();
		
		timer.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
		
		return flags.getContinents();
	}
	
	@PostMapping("/triggerETL")
	@ResponseStatus(HttpStatus.OK)
	public void triggerETL (@RequestBody EtlRequestDto input) {
		
		logger.info("Trigger ETL. full refresh : " +input.isFullRefresh());

		if(!input.isFullRefresh()) {
			return;
		}
		
		Flags f = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream is = null;
		try {
			flagMetrics.incrementContinent("Trigger ETL");
			is = this.getClass().getClassLoader().
					getResourceAsStream("continents.json");
			
			f = new Flags();
			List<Continent> clist = mapper.readValue(is, 
					new TypeReference<List<Continent>>(){});
			f.getContinents().addAll(clist);
			searchDao.addContinents(f);
			
		} catch(Exception e) {
			logger.error("Error reading json file" ,e);
		}
	}

	public FlagSearchDao getSearchDao() {
		return searchDao;
	}

	public void setSearchDao(FlagSearchDao searchDao) {
		this.searchDao = searchDao;
	}

	public FlagSearchMetrics getFlagMetrics() {
		return flagMetrics;
	}

	public void setFlagMetrics(FlagSearchMetrics flagMetrics) {
		this.flagMetrics = flagMetrics;
	}

}
