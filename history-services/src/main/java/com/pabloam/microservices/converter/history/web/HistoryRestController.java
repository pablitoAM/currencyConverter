package com.pabloam.microservices.converter.history.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.history.services.HistoryServices;

import rx.Observable;

@RestController
public class HistoryRestController {

	@Autowired
	private HistoryServices historyServices;

	/**
	 * Returns the name of the provider implementing the provider services
	 * 
	 * @return
	 */
	// @RequestMapping(value = "/history/getLast/{number}/{username}", method =
	// RequestMethod.GET)
	// public Observable<String> getLastQueriesOf(@PathVariable int number,
	// @PathVariable String userName) {
	// return Observable.just(historyServices.getLastQueriesOf(number,
	// userName));
	// }

}
