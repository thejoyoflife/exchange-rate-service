package com.shimul.quarkus.ers.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionDTO (
		@JsonProperty("Timestamp")
		LocalDateTime timestamp,
		@JsonProperty("Error")
		ExceptionCode errCode,
		@JsonProperty("Messages")
		List<String> errors) {}
