package com.shimul.quarkus.ers.provider;

public class CurrencyNotSupportedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public CurrencyNotSupportedException(String msg) {
		super(msg);
	}
}	
