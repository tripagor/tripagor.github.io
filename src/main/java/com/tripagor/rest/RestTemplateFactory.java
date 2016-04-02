package com.tripagor.rest;

import static java.util.Arrays.asList;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {

	public RestTemplate get(String host, String clientId, String clientSecret,
			HttpMessageConverter<?>... messageConverters) {
		final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(host.concat("/oauth/token"));
		resource.setClientId(clientId);
		resource.setClientSecret(clientSecret);
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource);
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		restTemplate.setRequestFactory(requestFactory);

		restTemplate.getMessageConverters().addAll(asList(messageConverters));

		return restTemplate;
	}

	public RestTemplate get(HttpMessageConverter<?>... messageConverters) {
		RestTemplate restTemplate = new RestTemplate();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		restTemplate.setRequestFactory(requestFactory);

		restTemplate.getMessageConverters().addAll(asList(messageConverters));

		return restTemplate;
	}
}
