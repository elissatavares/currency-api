package br.com.ada.currencyapi.controller;

import br.com.ada.currencyapi.domain.ConvertCurrencyRequest;
import br.com.ada.currencyapi.domain.Currency;
import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.exception.CoinNotFoundException;
import br.com.ada.currencyapi.exception.CurrencyException;
import br.com.ada.currencyapi.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @AfterEach
    void tearDown() {
        currencyRepository.deleteAll();
    }


    @Test
    void testGetCurrencyReturns200() throws Exception {
        assertEquals(0, currencyRepository.count()) ;
        currencyRepository.save(Currency.builder().name("EUR").build());
        assertEquals(1, currencyRepository.count());

        mockMvc.perform(
                        get("/currency")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    void testCreateCurrencyReturns201() throws Exception {
        assertEquals(0, currencyRepository.count()) ;
        Currency currency = Currency.builder().name("EUR").build();

        var content = new ObjectMapper().writeValueAsString(currency);

        mockMvc.perform(
                        post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpectAll(status().isCreated())
                .andDo(print());
        assertEquals(1, currencyRepository.count());
    }

    @Test
    void testConvertCurrencyWhenExchangeRateSavedInDatabaseReturns200() throws Exception {
        assertEquals(0, currencyRepository.count());

        Map<String, BigDecimal> exchanges = new HashMap<>();
        exchanges.put("USD", BigDecimal.TWO);

        currencyRepository.save(Currency.builder().name("EUR").exchanges(exchanges).build());
        assertEquals(1, currencyRepository.count());

        ConvertCurrencyRequest request = new ConvertCurrencyRequest("EUR", "USD", new BigDecimal("5.00"));
        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        get("/currency/convert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.amount").value(10.00))
                .andDo(print());
    }

    @Test
    void testConvertCurrencyWhenApiRequestSucceedsReturns200() throws Exception {
        assertEquals(0, currencyRepository.count());

        ConvertCurrencyRequest request = new ConvertCurrencyRequest("EUR", "USD", BigDecimal.TEN);
        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        get("/currency/convert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(jsonPath("$.amount").isNumber())
                .andExpectAll(status().isOk())
                .andDo(print());
    }


    @Test
    void testDeleteCurrencyReturns200() throws Exception {
        assertEquals(0, currencyRepository.count());
        Currency currency = currencyRepository.save(Currency.builder().name("EUR").build());
        assertEquals(1, currencyRepository.count());

        mockMvc.perform(
                        delete("/currency/{id}", currency.getId())
                )
                .andExpectAll(status().isOk())
                .andDo(print());
        assertEquals(0, currencyRepository.count());
    }


    @Test
    void testCreateCurrencyWhenCurrencyAlreadyExistsReturns500() throws Exception {
        currencyRepository.save(Currency.builder().name("EUR").build());

        assertEquals(1, currencyRepository.count()) ;

        var content = new ObjectMapper().writeValueAsString(CurrencyRequest.builder().name("EUR").build());

        mockMvc.perform(
                        post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpectAll(status().isInternalServerError())
                .andExpect(result -> assertInstanceOf(CurrencyException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Coin already exists", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
        assertEquals(1, currencyRepository.count());
    }

    @Test
    void testCreateCurrencyAnnotationValidReturns400() throws Exception {
        assertEquals(0, currencyRepository.count()) ;
        CurrencyRequest currencyRequest = new CurrencyRequest();

        var content = new ObjectMapper().writeValueAsString(currencyRequest);

        mockMvc.perform(
                        post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpectAll(status().isBadRequest())
                .andExpectAll(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andDo(print());
        assertEquals(0, currencyRepository.count());
    }

    @Test
    void testConvertCurrencyWhenCurrencyDoesNotExistReturns404() throws Exception {
        assertEquals(0, currencyRepository.count());
        String json = "{\"status\":404,\"code\":\"CoinNotExists\",\"message\":\"moeda nao encontrada NAOEXISTE-USD\"}";

        ConvertCurrencyRequest request = new ConvertCurrencyRequest("naoExiste", "USD", BigDecimal.TEN);
        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        get("/currency/convert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpectAll(status().isNotFound())
                .andExpectAll(result -> assertEquals("Error while getting response from Awesome API: " + json, result.getResolvedException().getMessage()))
                .andExpectAll(result -> assertInstanceOf(CoinNotFoundException.class, result.getResolvedException()))
                .andDo(print());
    }

}
