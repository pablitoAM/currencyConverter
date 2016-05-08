package com.pabloam.microservices.converter.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	protected void registerAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// @formatter:off
		http
//		.httpBasic().disable()
		.antMatcher("/**").authorizeRequests()
		.antMatchers("/oauth/authorize", "/oauth/confirm_access", "/register", "/webjars/**", "/css/**", "/json/**").permitAll()
		.anyRequest().authenticated()		
		.and()
			.formLogin()
        	.loginPage("/login")        	                    
        	.usernameParameter("f_un")
        	.passwordParameter("f_pw")
        	.permitAll()
        .and()
        	.logout()
        	.logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true);
        
		// @formatter:off	
	}	
}
