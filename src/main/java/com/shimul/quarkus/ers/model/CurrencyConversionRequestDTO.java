package com.shimul.quarkus.ers.model;

import java.math.BigDecimal;

public record CurrencyConversionRequestDTO(
						String fromCurr,
						String toCurr,
						BigDecimal amount
						) {}
