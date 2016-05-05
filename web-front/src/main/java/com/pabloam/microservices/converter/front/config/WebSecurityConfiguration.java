package com.pabloam.microservices.converter.front.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.antMatcher("/**")
				.authorizeRequests()
			.antMatchers("/", "/index", "/login", "/logout", "/register", "/webjars/**", "/auth/**", "/css/**", "/json/**")
				.permitAll()
			.anyRequest()
				.authenticated()
			.and()
				.csrf().disable();				
		//@formatter:on
	}
}
