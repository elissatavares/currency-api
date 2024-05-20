package br.com.ada.currencyapi.service;

import java.math.BigDecimal;
import java.util.*;

import br.com.ada.currencyapi.client.AwesomeClient;
import br.com.ada.currencyapi.domain.*;
import br.com.ada.currencyapi.domain.Currency;
import org.springframework.stereotype.Service;

import br.com.ada.currencyapi.exception.CoinNotFoundException;
import br.com.ada.currencyapi.exception.CurrencyException;
import br.com.ada.currencyapi.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final AwesomeClient awesomeClient;

    public List<CurrencyResponse> get() {
        List<Currency> currencies = currencyRepository.findAll();
        List<CurrencyResponse> dtos = new ArrayList<>();

        currencies.forEach((currency) -> dtos.add(CurrencyResponse.builder()
                .label("%s - %s".formatted(currency.getId(), currency.getName()))
                .build()));

        return dtos;
    }

    public Long create(CurrencyRequest request)  {
        Currency currency = currencyRepository.findByName(request.getName());

        if (Objects.nonNull(currency)) {
            throw new CurrencyException("Coin already exists");
        }

        Currency saved = currencyRepository.save(Currency.builder()
                .name(request.getName())
                .description(request.getDescription())
                .exchanges(request.getExchanges())
                .build());
        return saved.getId();
    }

    public void delete(Long id) {
        currencyRepository.deleteById(id);
    }

    public ConvertCurrencyResponse convert(ConvertCurrencyRequest request) {
        BigDecimal amount = getAmount(request);
        return ConvertCurrencyResponse.builder()
                .amount(amount)
                .build();
    }

    private BigDecimal getAmount(ConvertCurrencyRequest request) {
        Currency currency = currencyRepository.findByName(request.getFrom());
        boolean savedInDb = validateSavedInDb(currency, request);

        if(savedInDb){
            BigDecimal exchange = currency.getExchanges().get(request.getTo());
            return request.getAmount().multiply(exchange);
        }
        return getAmountWithAwesome(request);
    }

    private boolean validateSavedInDb(Currency currency, ConvertCurrencyRequest request) {
        try{
           return currency.getExchanges().containsKey(request.getTo());
        } catch (Exception e) {
            return false;
        }
    }

    private BigDecimal getAmountWithAwesome(ConvertCurrencyRequest request) {
        String coins = String.format("%s-%s", request.getFrom(), request.getTo());
        Map<String, ResponseAwesomeClient> response;

        try {
             response = awesomeClient.get(coins).getBody();
        } catch (Exception e) {
            String json = e.getMessage().replaceAll(".*?(\\{.*?\\}).*", "$1");
            throw new CoinNotFoundException("Error while getting response from Awesome API: " + json);
        }

        String key = coins.replace("-", "");
        BigDecimal exchange = new BigDecimal(response.get(key).getHigh());
        return request.getAmount().multiply(exchange);
    }

}

