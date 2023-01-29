package com.shimul.quarkus.ers.provider.ecb;

public class ProviderUtils {
	public static final String BASE_CURRENCY = "EUR";
	
	public static final String ZONE_ID = "CET";
	
	public static final String QUARTZ_CRON_EXP_DOWNLOAD_SCHEDULE = "0 5 16 ? * MON-FRI";
	
	public static final String CUBE_XML_ELEM = "Cube", DATE_XML_ATTR = "time", 
									CURRENCY_XML_ATTR = "currency", RATE_XML_ATTR = "rate";
	
	public static boolean isBlank(String s) {
		return s == null || s.isBlank();
	}
}
