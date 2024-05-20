package br.com.ada.currencyapi.client;

import br.com.ada.currencyapi.domain.ResponseAwesomeClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "AwesomeClient", url = "https://economia.awesomeapi.com.br")
public interface AwesomeClient {

    @GetMapping("json/last/{coins}")
    ResponseEntity<Map<String, ResponseAwesomeClient>> get(@PathVariable("coins") String coins);
}
