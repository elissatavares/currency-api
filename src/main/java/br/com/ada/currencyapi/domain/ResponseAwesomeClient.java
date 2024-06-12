package br.com.ada.currencyapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Override
    public String toString() {
        return "ResponseAwesomeClient{" +
                "ask='" + ask + '\'' +
                ", code='" + code + '\'' +
                ", codein='" + codein + '\'' +
                ", name='" + name + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", varBid='" + varBid + '\'' +
                ", pctChange='" + pctChange + '\'' +
                ", bid='" + bid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}