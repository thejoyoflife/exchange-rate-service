package com.shimul.quarkus.ers.exception;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.shimul.quarkus.ers.provider.CurrencyNotSupportedException;

public class ExchangeRateResourceExceptionMapper {

	@ServerExceptionMapper
	public RestResponse<ExceptionDTO> mapCurrencyNotFoundException(CurrencyNotSupportedException cns) {
		var resp = new ExceptionDTO(LocalDateTime.now(), 
									ExceptionCode.CURRENNCY_NOT_SUPPORTED,
									List.of(cns.getMessage()));
		return RestResponse.status(Response.Status.NOT_FOUND, resp);
	}
	
	@ServerExceptionMapper
	public RestResponse<ExceptionDTO> mapGeneralException(Exception ex) {
		var code = ExceptionCode.GENERAL_EXCEPTION;
		var msgs = List.of(ex.getMessage());
		var status = Response.Status.INTERNAL_SERVER_ERROR;
		
		if (ex.getCause() instanceof UnknownHostException uhs) {
			code = ExceptionCode.EXCHANGE_RATE_PROVIDER_UNAVAILABLE;
			status = Response.Status.SERVICE_UNAVAILABLE;
		}
		
		var resp = new ExceptionDTO(LocalDateTime.now(), 
									code,
									msgs);
		return RestResponse.status(status, resp);
	}
}
