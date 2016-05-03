/**
 * Copyright (c) 2016 Molenaar Strategie BV.
 * Created: 3 May 2016 13:56:01 Author: Pablo
 */

package com.pabloam.microservices.converter.provider.web;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import rx.Observable;

//===============================
// This part encapsulates the usage of Spring deferred result into
// observable
// Source: https://github.com/shazin/reactiveapp
// ===============================
public class ObservableReturnValueHandler implements HandlerMethodReturnValueHandler {

	public boolean supportsReturnType(MethodParameter returnType) {
		Class parameterType = returnType.getParameterType();
		return Observable.class.isAssignableFrom(parameterType);
	}

	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
			throws Exception {
		if (returnValue == null) {
			mavContainer.setRequestHandled(true);
			return;
		}

		final DeferredResult<Object> deferredResult = new DeferredResult<Object>();
		Observable observable = (Observable) returnValue;
		observable.subscribe(result -> deferredResult.setResult(result), errors -> deferredResult.setErrorResult(errors));

		WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, mavContainer);

	}
}
