package com.shimul.quarkus.ers.provider.ecb;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record ExchangeRateXMLParseResult(Map<String, BigDecimal> rates, LocalDate asonDate) {}
