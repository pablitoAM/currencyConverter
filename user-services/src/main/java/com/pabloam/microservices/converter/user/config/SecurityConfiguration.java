package com.pabloam.microservices.converter.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	protected void registerAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	@Order(2)
	public void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.antMatcher("/**")
				.authorizeRequests()
			.antMatchers("/register")
				.permitAll()
			.anyRequest()
				.authenticated()
			.and()
				.csrf().disable();				
		//@formatter:on
	}

}
