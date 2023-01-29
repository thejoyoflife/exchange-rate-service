package com.shimul.quarkus.ers;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import com.shimul.quarkus.ers.model.CurrencyConversionDTO;
import com.shimul.quarkus.ers.model.CurrencyConversionRequestDTO;
import com.shimul.quarkus.ers.model.CurrencyExchangeRateDTO;

import io.smallrye.mutiny.Uni;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeRateResource {
	
	@ConfigProperty(name = "currency.chart.url")
	String chartUrl;
	
	@Inject
	ExchangeRateService service;
	
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        return Uni.createFrom()
                    .item("Service is running fine");
    }
    
    @GET
    @Path("/rate")
    public RestResponse<CurrencyExchangeRateDTO> rateFor(@RestQuery String fromCurr, 
    					  				   				 @RestQuery String toCurr) {
    	var rate = service.getRateForCurrencies(fromCurr, toCurr);
    	return RestResponse.ok(new CurrencyExchangeRateDTO(fromCurr, toCurr, rate));
    }
    
    @GET
    @Path("/currencies")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Map<String, Long>> supportedCurrencies() {
    	var res = service.getCurrencyListAndHitCount();
    	return RestResponse.ok(res);
    }
    
    @POST
    @Path("/convert")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<CurrencyConversionDTO> convertAmount(CurrencyConversionRequestDTO convReq) {
		var resp = service.getConvertedAmount(convReq);
    	return RestResponse.ok(resp);
    }
    
    @GET
    @Path("/chart")
    @Produces(MediaType.TEXT_PLAIN)
    public RestResponse<String> exchangeRateChartUrl(@RestQuery String fromCurr,
    									             @RestQuery String toCurr) {
    	var resp = new StringBuilder()
		    			.append(chartUrl)
		    			.append("from=").append(fromCurr)
		    			.append("&to=").append(toCurr)
		    			.toString();
    	return RestResponse.ok(resp);
    }
}