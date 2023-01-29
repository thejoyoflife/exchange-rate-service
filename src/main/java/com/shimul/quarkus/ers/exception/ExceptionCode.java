package com.shimul.quarkus.ers.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExceptionCode {
	CURRENNCY_NOT_SUPPORTED("01", "Given Currency Combination is not supported"),
	EXCHANGE_RATE_PROVIDER_UNAVAILABLE("02", "Currency exchange rate provider is unavailable"),
	GENERAL_EXCEPTION ("99", "General Exception Occurred. Please contact support.");
	
	private String code, msg;
	
	private ExceptionCode(String errorCode, String errorMsg) {
		this.code = errorCode;
		this.msg = errorMsg;
	}
	
	@JsonGetter("Code")
	public String getErrorCode() {
		return code;
	}
	
	@JsonGetter("Description")
	public String getErrorMsg() {
		return msg;
	}
}
