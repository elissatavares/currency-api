package br.com.ada.currencyapi.exception;

public record ErrorObject(String message, String field, Object rejectedValue) {

}
