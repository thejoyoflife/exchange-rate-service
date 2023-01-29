package com.shimul.quarkus.ers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.shimul.quarkus.ers.TestUtils.EUR_TO_DKK_RATE;
import static com.shimul.quarkus.ers.TestUtils.EUR_TO_USD_RATE;

import java.util.Collections;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WiremockExtensions implements QuarkusTestResourceLifecycleManager {
	private WireMockServer wiremockServer;
	
	@Override
	public Map<String, String> start() {
		wiremockServer = new WireMockServer(0); // "0" means random port
		wiremockServer.start();
		
		wiremockServer.stubFor(get(urlEqualTo("/eurofxref-daily.xml"))
								.willReturn(
										aResponse()
										.withBody("""
												<?xml version="1.0" encoding="UTF-8"?>
													<gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
														<gesmes:subject>Reference rates</gesmes:subject>
														<gesmes:Sender>
															<gesmes:name>European Central Bank</gesmes:name>
														</gesmes:Sender>
														<Cube>
															<Cube time='2023-01-27'>
																<Cube currency='USD' rate='%f'/>
																<Cube currency='DKK' rate='%f'/>																		
															</Cube>
														</Cube>
													</gesmes:Envelope>
												""".formatted(EUR_TO_USD_RATE, EUR_TO_DKK_RATE))));
		return Collections.singletonMap("quarkus.rest-client.ecb-exchg.url", wiremockServer.baseUrl());
	}

	@Override
	public void stop() {
		if (null != wiremockServer) {
			wiremockServer.stop();
		}
	}
}
