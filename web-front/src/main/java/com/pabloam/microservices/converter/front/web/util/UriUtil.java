package com.pabloam.microservices.converter.front.web.util;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UriUtil {

	/**
	 * Composes a uri from a request with the following path
	 * 
	 * @param request
	 * @return
	 */
	public URI composeUri(HttpServletRequest request, String path) {
		URI uri = UriComponentsBuilder.newInstance().scheme(request.getScheme()).host(request.getServerName()).port(request.getServerPort()).path(path).build()
				.toUri();
		return uri;
	}
}
