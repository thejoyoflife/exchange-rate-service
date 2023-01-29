package com.shimul.quarkus.ers.provider.ecb;

import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.BASE_CURRENCY;
import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.isBlank;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.shimul.quarkus.ers.provider.CurrencyNotSupportedException;
import com.shimul.quarkus.ers.provider.IExchangeRateProvider;

@ApplicationScoped
public class ExchangeRateProvider implements IExchangeRateProvider {

	@Inject
	ExchangeRateProviderService remoteService;
	
	private static final Supplier<CurrencyNotSupportedException> EXCEPTION_SUPPLIER = 
							() -> new CurrencyNotSupportedException("Given currency combination is not supported");
	
	@Override
	public List<String> supportedCurrencies() {
		return remoteService.getSupportedCurrencies();
	}
	
	@Override
	public BigDecimal rateFor(String frmCurr, String toCurr) throws CurrencyNotSupportedException {
		String fromCurr = isBlank(frmCurr) ? BASE_CURRENCY : frmCurr;
		return remoteService.getCurrencyRate(fromCurr, toCurr).orElseThrow(EXCEPTION_SUPPLIER);
	}
}
