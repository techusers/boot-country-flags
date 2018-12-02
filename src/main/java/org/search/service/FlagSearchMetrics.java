package org.search.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class FlagSearchMetrics {
	
	private final MeterRegistry registry;
	private static final String CONTINENT_COUNTER_PREFIX = "flag.search.continent.";
	private static final String COUNTRY_COUNTER_PREFIX = "flag.search.country.";
	
	private Map<String,Counter> countersMap = new HashMap<String, Counter>();
	
	public FlagSearchMetrics(MeterRegistry registry) {
		this.registry = registry;
	}
	
	public void incrementContinent(String metric) {
		metric = CONTINENT_COUNTER_PREFIX + metric;
		increment(metric);
	}
	
	public void incrementCountry(String metric) {

		metric = COUNTRY_COUNTER_PREFIX + metric;
		increment(metric);
	}

	private void increment(String metric) {

		Counter c = countersMap.get(metric);
		
		if(c == null) {
			c = registry.counter(metric);
			countersMap.put(metric, c);
		}
		
		c.increment();
	}
}
