package com.shimul.quarkus.ers.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyConversionDTO (
						@JsonProperty("FromCurr")
						String fromCurr,
						@JsonProperty("ToCurr")
						String toCurr,
						@JsonProperty("Amount")
						BigDecimal amount,
						@JsonProperty("ConvertedAmount")
						BigDecimal convertedAmount
						) {}