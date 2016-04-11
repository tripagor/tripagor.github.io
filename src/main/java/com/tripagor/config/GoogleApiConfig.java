package com.tripagor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.maps.GeoApiContext;

@Configuration
public class GoogleApiConfig {
	private @Value("${google.maps.api.key}") String apiKey;

	@Bean
	public GeoApiContext geoApiContext() {
		return new GeoApiContext().setApiKey(this.apiKey);
	}
}
