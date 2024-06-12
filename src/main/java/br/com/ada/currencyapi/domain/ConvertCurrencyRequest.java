package br.com.ada.currencyapi.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ConvertCurrencyRequest(
        @NotBlank String from,
        @NotBlank String to,
        @PositiveOrZero BigDecimal amount) {}
