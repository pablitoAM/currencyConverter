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
		.authorizeRequests()
			.antMatchers("/", "/index", "/login", "/register", "/logout", "/auth/**", "/webjars/**", "/css/**", "/json/**").permitAll()			
			.anyRequest().authenticated()
//        .and()
//        .formLogin()
//            .loginPage("/") 
//            .usernameParameter("f_un")
//            .passwordParameter("f_pw") .loginProcessingUrl("/login").defaultSuccessUrl("/")        
            .and()
            	.exceptionHandling().accessDeniedPage("/index")
            .and().csrf().disable();                 
            
		//@formatter:on
	}
}
