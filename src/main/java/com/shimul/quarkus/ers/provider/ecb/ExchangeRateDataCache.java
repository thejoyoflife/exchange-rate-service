package com.shimul.quarkus.ers.provider.ecb;

import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.BASE_CURRENCY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExchangeRateDataCache {
	
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateDataCache.class);
	
	@Inject
	@RestClient
	RemoteWebServiceInterface ecb;
	
	@Inject
	ExchangeRateXMLDataParser rateParser;
	
	// currency rates against a base currency
	private final Map<String, BigDecimal> currRates = new ConcurrentHashMap<>();
	
	private LocalDate asonDate;
	
	public Optional<BigDecimal> getRateAgnstBaseCurr(String curr) {
		// An attempt to show most up-to-date data to the users
		if (isInvalid()) {
			refresh();
		}
		return Optional.ofNullable(currRates.get(curr.toUpperCase()));
	}
	
	public List<String> getAllCurrencies() {
		return new ArrayList<>(currRates.keySet());
	}
	
	/**
	 * To allow the scheduled job to refresh the cache no matter what happens.
	 * This is an effort to show up-to-date currency rates to the users.
	 */
	public void forceRefresh() {
		try {
			downloadAndFillupCache();
		} catch (Exception e) {
			LOGGER.error("Error in force refreshing the cache", e);
			// this is to prevent stale currency rates to be shown to the users.
			// now, every request for currency rates will try downloading
			// the data until one of them becomes successful.
			clear();
		}
	}
	
	private synchronized void refresh() {
		if (isInvalid()) {
			downloadAndFillupCache();
		}
	}
	
	private void downloadAndFillupCache() {
		var xml = ecb.getExchangeRateXml();
		var rateData = rateParser.parseXMLData(xml);
		rateData.ifPresent(res -> {
			clear();
			currRates.putAll(res.rates());
			// adding the rate of base currency against itself as "1" into the cache
			currRates.put(BASE_CURRENCY, new BigDecimal("1"));
			asonDate = res.asonDate();
		});
	}
	
	private void clear() {
		currRates.clear();
		asonDate = null;
	}

	private boolean isInvalid() {
		return asonDate == null;
	}
}
