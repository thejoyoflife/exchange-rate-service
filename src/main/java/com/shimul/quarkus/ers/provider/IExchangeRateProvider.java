package com.shimul.quarkus.ers.provider;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public interface IExchangeRateProvider {
	
	public static final int PRECISION_LENGTH = 7;
	public static final MathContext ROUNDING_CONTEXT 
							= new MathContext(PRECISION_LENGTH, RoundingMode.HALF_EVEN);
	/**
	 * 
	 * @return list of currencies supported
	 */
	public List<String> supportedCurrencies();

	/**
	 * 
	 * @param frmCurr it defaults to base currency supported by the provider if not given
	 * @param toCurr must be non-{@code null}
	 * @return the exchange rate
	 * @throws CurrencyNotSupportedException if any of the given currency is not supported by the provider 
	 */
	public BigDecimal rateFor(String frmCurr, String toCurr) throws CurrencyNotSupportedException;
}
