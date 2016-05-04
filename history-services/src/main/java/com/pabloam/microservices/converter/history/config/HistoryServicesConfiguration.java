package com.pabloam.microservices.converter.history.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.pabloam.microservices.converter.history.web.ObservableReturnValueHandler;

@Configuration
public class HistoryServicesConfiguration {

	// ===============================
	// This part encapsulates the usage of Spring deferred result into
	// observable
	// Source: https://github.com/shazin/reactiveapp
	// ===============================

	@Resource
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	@PostConstruct
	public void init() {
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList(requestMappingHandlerAdapter.getReturnValueHandlers());
		handlers.add(0, observableReturnValueHandler());
		requestMappingHandlerAdapter.setReturnValueHandlers(handlers);
	}

	@Bean
	public HandlerMethodReturnValueHandler observableReturnValueHandler() {
		return new ObservableReturnValueHandler();
	}

}
