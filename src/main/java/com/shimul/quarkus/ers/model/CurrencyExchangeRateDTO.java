package com.shimul.quarkus.ers.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyExchangeRateDTO (
		@JsonProperty("FromCurr")
		String fromCurr,
		@JsonProperty("ToCurr")
		String toCurr,
		@JsonProperty("Rate")
		BigDecimal rate) {}
