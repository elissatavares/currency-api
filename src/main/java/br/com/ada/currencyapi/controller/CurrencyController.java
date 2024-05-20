package br.com.ada.currencyapi.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        List<CurrencyResponse> currencies = currencyService.get();
        return new ResponseEntity<>(currencyService.get(), HttpStatus.OK);
    }

    @Operation(summary = "Convert currency", description = "Convert an amount from one currency to another")
    @GetMapping("/convert")
    public ResponseEntity<ConvertCurrencyResponse> convert(@RequestBody ConvertCurrencyRequest request) {
        ConvertCurrencyResponse response = currencyService.convert(request);
        return new ResponseEntity<>(currencyService.convert(request), HttpStatus.OK);
    }

    @Operation(summary = "Create a new currency", description = "Add a new currency to the system")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CurrencyRequest request) {
        Long id = currencyService.create(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a currency by ID", description = "Remove a currency from the system by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        currencyService.delete(id);
        return ResponseEntity.ok().build();
    }
}