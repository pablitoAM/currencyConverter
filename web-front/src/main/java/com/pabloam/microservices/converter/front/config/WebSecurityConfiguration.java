package com.pabloam.microservices.converter.front.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2ClientContextFilter oauth2ClientFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//@formatter:off
			http
//			.httpBasic().disable()
		      .antMatcher("/**")
		      .authorizeRequests()
		        .antMatchers("/", "/login", "/index", "/auth/**", "/resource/**", "/webjars/**", "/css/**", "/json/**")
		        .permitAll()		    		      	
		      .anyRequest().authenticated().and()
		      .addFilterAfter(oauth2ClientFilter, ExceptionTranslationFilter.class)
		      	.logout().logoutSuccessUrl("/").permitAll();		
		//@formatter:on
	}

}
