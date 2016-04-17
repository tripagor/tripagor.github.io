package com.tripagor;

import static java.util.Arrays.asList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagor.hotels.model.Hotel;

public class GoogleKeywordPlannerFile {

	public static void main(String args[]) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());
		MappingJackson2HttpMessageConverter hateoasConverter = new MappingJackson2HttpMessageConverter();
		hateoasConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		hateoasConverter.setObjectMapper(mapper);
		RestTemplate restTemplate = new RestTemplate();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		restTemplate.setRequestFactory(requestFactory);
		restTemplate.setMessageConverters(asList(hateoasConverter));

		int currentPage = 0;
		long totalPages = 25;
		List<String> citynames = new ArrayList<>();
		while (currentPage < totalPages) {
			PagedResources<Hotel> result = restTemplate
					.exchange("http://api.tripagor.com/hotels?page={page}&size={pageSize}", HttpMethod.GET, null,
							new ParameterizedTypeReference<PagedResources<Hotel>>() {
							}, currentPage++, 200)
					.getBody();

			//totalPages = result.getMetadata().getTotalPages();
			Collection<Hotel> hotels = result.getContent();

			for (Hotel hotel : hotels) {
				if (!citynames.contains(hotel.getCityUnique())) {
					citynames.add(hotel.getCityUnique());
				}
			}
		}
		
		int entyrSize = 800;
		int currentChunk = 0;
		String citystr = "";
		while (currentChunk < Math.ceil(new Double(citynames.size()) / new Double(entyrSize))) {
			int end = (currentChunk + 1) * entyrSize;
			if(end > citynames.size()){
				end = citynames.size();
			}
			
			for (String city : citynames.subList(currentChunk * entyrSize, end)) {
				citystr += "hotels " + city + "\n";
			}

			Path file = Paths.get("target", "hotel_" + currentChunk + ".csv");
			Files.write(file, citystr.getBytes());
			currentChunk++;
			citystr = "";
		}

	}
}