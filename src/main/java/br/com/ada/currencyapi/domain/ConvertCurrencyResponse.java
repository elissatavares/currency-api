package br.com.ada.currencyapi.domain;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ConvertCurrencyResponse(BigDecimal amount) {}
