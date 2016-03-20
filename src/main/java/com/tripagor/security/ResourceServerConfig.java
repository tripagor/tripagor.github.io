package com.tripagor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Autowired
	private TokenStore tokenStore;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("auth").tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/hotels/**").access("#oauth2.hasScope('remove')")
				.antMatchers(HttpMethod.GET, "/hotels/**").access("#oauth2.hasScope('get')")
				.antMatchers(HttpMethod.POST, "/hotels/**").access("#oauth2.hasScope('create')")
				.antMatchers(HttpMethod.PUT, "/hotels/**").access("#oauth2.hasScope('change')")
				.antMatchers(HttpMethod.PATCH, "/hotels/**").access("#oauth2.hasScope('change')");
	}
}