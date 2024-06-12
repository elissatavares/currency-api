package br.com.ada.currencyapi.domain;

import java.math.BigDecimal;
import java.util.HashMap;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CurrencyRequest(
        @NotBlank
        String name,
        String description, HashMap<String, BigDecimal> exchanges) {}