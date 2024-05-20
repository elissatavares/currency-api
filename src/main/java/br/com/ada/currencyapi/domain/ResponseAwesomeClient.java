package br.com.ada.currencyapi.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAwesomeClient {
    @Override
    public String toString() {
        return "conversionReturnData{" +
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
                ", create_date='" + createDate + '\'' +
                '}';
    }

    private String code;

    private String codein;

    private String name;

    private String high;

    private String low;

    private String varBid;

    private String pctChange;

    private String bid;

    private String ask;

    private String timestamp;

    @JsonProperty("create_date")
    private String createDate;
}
