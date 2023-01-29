package com.shimul.quarkus.ers.provider.ecb;

import static com.shimul.quarkus.ers.provider.IExchangeRateProvider.ROUNDING_CONTEXT;
import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.isBlank;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.shimul.quarkus.ers.provider.ecb.cache.ExchangeRateDataCache;

@ApplicationScoped
public class ExchangeRateProviderService {
	
	@Inject
	ExchangeRateDataCache cache;
	
	public Optional<BigDecimal> getCurrencyRate(String frmCurr, String toCurr) {
		if (isBlank(frmCurr) || isBlank(toCurr)) {
			return Optional.empty();
		}
		
		Optional<BigDecimal> fromRateAgnstBaseCurr = cache.getRateAgnstBaseCurr(frmCurr);
		Optional<BigDecimal> toRateAgnstBaseCurr = cache.getRateAgnstBaseCurr(toCurr); 
		
		BigDecimal result = null;
		
		if (fromRateAgnstBaseCurr.isPresent() && toRateAgnstBaseCurr.isPresent()) {
			result = toRateAgnstBaseCurr.get().divide(fromRateAgnstBaseCurr.get(), ROUNDING_CONTEXT);
		}
		
		return Optional.ofNullable(result);
	}
	
	public List<String> getSupportedCurrencies() {
		return cache.getAllCurrencies();
	}
}
