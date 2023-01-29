package com.shimul.quarkus.ers.provider.ecb.xml;

import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.CUBE_XML_ELEM;
import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.CURRENCY_XML_ATTR;
import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.DATE_XML_ATTR;
import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.RATE_XML_ATTR;

import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;

import org.jboss.logging.Logger;

@ApplicationScoped
public class ExchangeRateXMLDataParser {
	
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateXMLDataParser.class);
	
	public Optional<ExchangeRateXMLParseResult> parseXMLData(String xml) {
		var rates = new HashMap<String, BigDecimal>();
		LocalDate asonDate = null;
		try {
			// parse the rates xml data using StAX method
			var xmlInputFactory = XMLInputFactory.newInstance();
			var xmlReader = xmlInputFactory.createXMLEventReader(new StringReader(xml));
			while(xmlReader.hasNext()) {
				var xmlEvent = xmlReader.nextEvent();
				if (xmlEvent.isStartElement()) {
					var startElem = xmlEvent.asStartElement();
					if (CUBE_XML_ELEM.equals(startElem.getName().getLocalPart())) {
						var timeAttr = startElem.getAttributeByName(new QName(DATE_XML_ATTR));
						if (timeAttr != null) {
							asonDate = LocalDate.parse(timeAttr.getValue());
						}
						var currencyAttr = startElem.getAttributeByName(new QName(CURRENCY_XML_ATTR));
						if (currencyAttr != null) {
							var rateAttr = startElem.getAttributeByName(new QName(RATE_XML_ATTR));
							rates.put(currencyAttr.getValue().toUpperCase(), new BigDecimal(rateAttr.getValue()));
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in parsing rate xml", e);
		}
		
		if (asonDate != null && rates.size() > 0) {
			return Optional.of(new ExchangeRateXMLParseResult(rates, asonDate));
		}
		return Optional.empty();
	}
}
