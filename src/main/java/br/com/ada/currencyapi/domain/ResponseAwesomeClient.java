package br.com.ada.currencyapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ResponseAwesomeClient(
        String ask,
        String code,
        String codein,
        String name,
        String high,
        String low,
        String varBid,
        String pctChange,
        String bid,
        String timestamp,
        @JsonProperty("create_date") String createDate) {
}