package com.shimul.quarkus.ers;

import static com.shimul.quarkus.ers.provider.IExchangeRateProvider.ROUNDING_CONTEXT;
import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.shimul.quarkus.ers.model.CurrencyConversionRequestDTO;
import com.shimul.quarkus.ers.model.CurrencyConversionDTO;
import com.shimul.quarkus.ers.provider.CurrencyNotSupportedException;
import com.shimul.quarkus.ers.provider.IExchangeRateProvider;

@ApplicationScoped
public class ExchangeRateService {	
	
	private Map<String, Long> hitMap = new ConcurrentHashMap<>();
	
	@Inject
	IExchangeRateProvider exRateProvider;

	public BigDecimal getRateForCurrencies(String frmCurr, String toCurr) 
											throws CurrencyNotSupportedException {
		incrementHitCount(frmCurr, toCurr);
		return exRateProvider.rateFor(frmCurr, toCurr);
	}
	
	public CurrencyConversionDTO getConvertedAmount(CurrencyConversionRequestDTO convReq) 
														throws CurrencyNotSupportedException {
		var rate = getRateForCurrencies(convReq.fromCurr(), convReq.toCurr());
		var convAmount = rate.multiply(convReq.amount(), ROUNDING_CONTEXT);
		return new CurrencyConversionDTO(convReq.fromCurr(), 
										 convReq.toCurr(), 
										 convReq.amount(), 
										 convAmount);
	}	
	
	public Map<String, Long> getCurrencyListAndHitCount() {
		return exRateProvider.supportedCurrencies()
				.stream()
				.collect(toMap(k -> k, k -> hitMap.getOrDefault(k, 0l)));
	}
	
	private void incrementHitCount(String ...currencies) {
		Arrays.stream(currencies)
			   .filter(c -> c != null)	
			   .forEach(currency -> 
						hitMap.compute(currency, (k, v) -> v == null ? 1l : v + 1l));
	}	
}
