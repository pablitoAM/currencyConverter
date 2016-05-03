package com.pabloam.microservices.converter.history.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.common.CurrencyQuery;
import com.pabloam.microservices.converter.history.services.HistoryServices;

import rx.Observable;

@RestController
public class HistoryRestController {

	@Autowired
	private HistoryServices historyServices;

	/**
	 * Returns the last number queries for the given user
	 * 
	 * @return
	 */
	@RequestMapping(value = "/history/getLast/{number}/{username}", method = RequestMethod.GET)
	public Observable<List<CurrencyQuery>> getLastQueriesOf(@PathVariable int number, @PathVariable String userName) {
		return Observable.just(historyServices.getLastQueriesOf(number, userName));
	}

}
