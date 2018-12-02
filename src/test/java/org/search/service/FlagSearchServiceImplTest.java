package org.search.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.search.dao.FlagSearchDao;
import org.search.dto.Continent;
import org.search.dto.Country;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;


public class FlagSearchServiceImplTest {

	//@Mock
    //private FlagSearchDao mockDao;
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
 
	//unit test flagsearchservice.getcountry is 
	//ideally need more functional tests since the search service is thin
    @Test
    public void testretrievecountry() {
    	
    		FlagSearchDao mockDao = mock(FlagSearchDao.class);
    		MeterRegistry mockregistry = mock(MeterRegistry.class);
    		FlagSearchMetrics mockMetrics = mock(FlagSearchMetrics.class);
    		Timer mocktimer = mock(Timer.class);

    		FlagSearchServiceImpl searchService = new FlagSearchServiceImpl(mockregistry);
    		
    		when(mockregistry.timer(org.mockito.ArgumentMatchers.anyString())).thenReturn(mocktimer);
    		when(mockDao.getCountry("America")).thenReturn(buildMockCountry());
    		
    		
    		searchService.setSearchDao(mockDao);
    		searchService.setFlagMetrics(mockMetrics);
    		Country c = searchService.getCountry("America");
    		
    		assertNotNull(c);//verify that get country is executed without errors
    		
    	
    }
    
  //unit test flagsearchservice.getcontinent is 
  	//ideally need more functional tests since the search service is thin
    @Test
    public void testretrievecontinent() {
    	
    		FlagSearchDao mockDao = mock(FlagSearchDao.class);
    		MeterRegistry mockregistry = mock(MeterRegistry.class);
    		FlagSearchMetrics mockMetrics = mock(FlagSearchMetrics.class);
    		Timer mocktimer = mock(Timer.class);

    		FlagSearchServiceImpl searchService = new FlagSearchServiceImpl(mockregistry);
    		
    		when(mockregistry.timer(org.mockito.ArgumentMatchers.anyString())).thenReturn(mocktimer);
    		when(mockDao.getContinent("Africa")).thenReturn(buildMockContinent());
    		
    		
    		searchService.setSearchDao(mockDao);
    		searchService.setFlagMetrics(mockMetrics);
    		Continent c = searchService.getContinent("Africa");
    		
    		assertNotNull(c);//Verify that the get continent is executed without errors
 
    }
    
    private Country buildMockCountry() {
    	Country c = new Country();
    	c.setName("America");
    	c.setFlag("AFlag");
    	return c;
    }
    
    private Continent buildMockContinent() {
    	Continent c = new Continent();
    	c.setContinent("Africa");
    	c.getCountries().add(buildMockCountry());
    	return c;
    }
}
