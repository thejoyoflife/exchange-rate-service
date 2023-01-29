package com.shimul.quarkus.ers.exception;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.shimul.quarkus.ers.provider.CurrencyNotSupportedException;

public class ExchangeRateResourceExceptionMapper {

	@ServerExceptionMapper
	public RestResponse<ExceptionDTO> mapGeneralException(RuntimeException ex) {
		var resp = new ExceptionDTO(LocalDateTime.now(), 
									ExceptionCode.GENERAL_EXCEPTION,
									List.of(ex.getMessage()));
		return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, resp);
	}
	
	@ServerExceptionMapper
	public RestResponse<ExceptionDTO> mapCurrencyNotFoundException(CurrencyNotSupportedException cns) {
		var resp = new ExceptionDTO(LocalDateTime.now(), 
									ExceptionCode.CURRENNCY_NOT_SUPPORTED,
									List.of(cns.getMessage()));
		return RestResponse.status(Response.Status.NOT_FOUND, resp);
	}
}
