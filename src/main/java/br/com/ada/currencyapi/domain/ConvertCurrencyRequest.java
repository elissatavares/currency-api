package br.com.ada.currencyapi.domain;

import java.math.BigDecimal;

public record ConvertCurrencyRequest(String from, String to, BigDecimal amount) {}
