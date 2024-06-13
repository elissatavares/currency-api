package br.com.ada.currencyapi.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.ada.currencyapi.client.AwesomeClient;
import br.com.ada.currencyapi.domain.ConvertCurrencyRequest;
import br.com.ada.currencyapi.domain.ConvertCurrencyResponse;
import br.com.ada.currencyapi.domain.Currency;
import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.domain.ResponseAwesomeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import br.com.ada.currencyapi.exception.CoinNotFoundException;
import br.com.ada.currencyapi.exception.CurrencyException;
import br.com.ada.currencyapi.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    private final AwesomeClient awesomeClient;

    public List<CurrencyResponse> get() {
        log.info("Fetching all currencies from the repository");
        List<Currency> currencies = currencyRepository.findAll();
        List<CurrencyResponse> dtos = new ArrayList<>();

        currencies.forEach(currency -> dtos.add(
                new CurrencyResponse(
                        "%s - %s".formatted(currency.getId(), currency.getName()))));
        log.info("Successfully fetched {} currencies", dtos.size());

        return dtos;
    }

    public Long create(CurrencyRequest request) {
        log.info("Creating a new currency with name: {}", request.name());
        Currency currency = currencyRepository.findByName(request.name());

        if (Objects.nonNull(currency)) {
            log.error("Currency with name '{}' already exists", request.name());
            throw new CurrencyException("Coin already exists");
        }

        Currency saved = currencyRepository.save(Currency.builder()
                .name(request.name())
                .description(request.description())
                .exchanges(request.exchanges())
                .build());
        log.info("Currency created successfully with ID: {}", saved.getId());

        return saved.getId();
    }

    public void delete(Long id) {
        log.info("Deleting currency with ID: {}", id);
        currencyRepository.deleteById(id);
        log.info("Currency with ID {} deleted successfully", id);
    }

    public ConvertCurrencyResponse convert(ConvertCurrencyRequest request) {
        log.info("Converting amount from {} to {}", request.from(), request.to());
        BigDecimal amount = getAmount(request);
        log.info("Conversion completed successfully. Converted amount: {}", amount);

        return new ConvertCurrencyResponse(amount);
    }

    private BigDecimal getAmount(ConvertCurrencyRequest request) {
        log.debug("Fetching exchange rate for conversion from {} to {}", request.from(), request.to());
        Currency currency = currencyRepository.findByName(request.from());

        boolean savedInDb = validateSavedInDb(currency, request);
        BigDecimal exchange = savedInDb ? currency.getExchanges().get(request.to()) : getAmountWithAwesome(request);

        return request.amount().multiply(exchange);
    }

    private boolean validateSavedInDb(Currency currency, ConvertCurrencyRequest request) {
        boolean isValid = Objects.nonNull(currency) &&
                Objects.nonNull(currency.getExchanges()) &&
                currency.getExchanges().containsKey(request.to());
        log.debug("Is exchange rate saved in database: {}", isValid);
        return isValid;
    }

    private BigDecimal getAmountWithAwesome(ConvertCurrencyRequest request) {
        Map<String, ResponseAwesomeClient> response;
        BigDecimal exchange;

        String coins = String.format("%s-%s", request.from(), request.to());
        String key = coins.replace("-", "");

        try {
            log.info("Fetching exchange rate from Awesome API for coins: {}", coins);
            response = awesomeClient.get(coins).getBody();
            exchange = new BigDecimal(response.get(key).high());
            log.info("Successfully fetched exchange rate: {}", exchange);
        } catch (Exception e) {
            log.error("Error fetching exchange rate from Awesome API for coins {}: {}", coins, e.getMessage());
            throw new CoinNotFoundException("Error while getting response from Awesome API");
        }

        return exchange;
    }
}
