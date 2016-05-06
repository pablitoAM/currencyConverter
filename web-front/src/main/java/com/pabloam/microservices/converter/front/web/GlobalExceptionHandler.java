package com.pabloam.microservices.converter.front.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Error Handling
	@ExceptionHandler(Exception.class)
	public ModelAndView handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("error", String.format("Exception in request: '%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
		return mv;
	}

}
