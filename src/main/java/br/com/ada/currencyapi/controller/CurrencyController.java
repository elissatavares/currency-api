package br.com.ada.currencyapi.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ada.currencyapi.domain.ConvertCurrencyRequest;
import br.com.ada.currencyapi.domain.ConvertCurrencyResponse;
import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Currency Management System", description = "Operations pertaining to currency management")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Operation(summary = "Get all currencies", description = "Retrieve a list of all available currencies")
    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> get() {
        log.info("Fetching all currencies");
        List<CurrencyResponse> currencies = currencyService.get();
        log.info("Fetched {} currencies", currencies.size());
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @Operation(summary = "Convert currency", description = "Convert an amount from one currency to another")
    @PostMapping("/convert")
    public ResponseEntity<ConvertCurrencyResponse> convert(@Valid @RequestBody ConvertCurrencyRequest request) {
        log.info("Converting currency: {}", request);
        ConvertCurrencyResponse response = currencyService.convert(request);
        log.info("Currency converted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new currency", description = "Add a new currency to the system")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CurrencyRequest request) {
        log.info("Creating new currency: {}", request);
        Long id = currencyService.create(request);
        log.info("Currency created with ID: {}", id);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a currency by ID", description = "Remove a currency from the system by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PositiveOrZero @PathVariable("id") Long id) {
        log.info("Deleting currency with ID: {}", id);
        currencyService.delete(id);
        log.info("Currency deleted successfully");
        return ResponseEntity.ok().build();
    }
}