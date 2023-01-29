package com.shimul.quarkus.ers.provider.ecb.remote;

import java.lang.reflect.Method;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;

@Path("/eurofxref-daily.xml")
@RegisterRestClient(configKey = "ecb-exchg")
public interface RemoteWebServiceInterface {

	@GET
	String getExchangeRateXml();
	
	// TODO: dummy exception handling (for 400, 500 codes)
	@ClientExceptionMapper
	static RuntimeException toException(Response resp, Method method) {
		String loc = method.toString();
		if (resp.getStatus() == 500) {
			throw new RuntimeException(loc + " [remote service] responded with an internal exception");
		}
		return null; // continue with the next exception mapper
	}
}
