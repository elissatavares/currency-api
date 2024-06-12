package br.com.ada.currencyapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import br.com.ada.currencyapi.domain.ConvertCurrencyRequest;
import br.com.ada.currencyapi.domain.ConvertCurrencyResponse;
import br.com.ada.currencyapi.exception.CoinNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.service.CurrencyService;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CurrencyControllerUnitTest {

    @InjectMocks
    private CurrencyController currencyController;

    @Mock
    private CurrencyService currencyService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    void convertCurrencyRequestValidation() throws Exception {
        // Request com campos em branco e amount negativo
        ConvertCurrencyRequest request = ConvertCurrencyRequest.builder()
                .from("")
                .to("")
                .amount(new BigDecimal("-100.00"))
                .build();
        var content = new ObjectMapper().writeValueAsString(request);

        // Envio da requisição POST para /currencies/convert
        mockMvc.perform(
                        post("/currency/convert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    void currencyRequestValidation() throws Exception {
        // Criando um objeto CurrencyRequest com campo 'name' em branco
        CurrencyRequest request = new CurrencyRequest("", "Description", new HashMap<>());
        var content = new ObjectMapper().writeValueAsString(request);

        // Envio da requisição POST para um endpoint fictício /currencies
        mockMvc.perform(
                        post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCurrenciesReturns200() throws Exception {
        Mockito.when(currencyService.get()).thenReturn(List.of(CurrencyResponse.builder()
                        .label("1 - USD")
                .build()));

        mockMvc.perform(
                        get("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].label").value("1 - USD"))
                .andDo(print());
    }


    @Test
    void testCreateCurrencyReturns201() throws Exception {
        Mockito.when(currencyService.create(any(CurrencyRequest.class))).thenReturn(5L);
        CurrencyRequest request = CurrencyRequest.builder()
                .name("USD")
                .build();
        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(5L))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void testConvertCurrencyReturns200() throws Exception {
        ConvertCurrencyResponse response = new ConvertCurrencyResponse(BigDecimal.TEN);

        Mockito.when(currencyService.convert(any(ConvertCurrencyRequest.class))).thenReturn(response);

        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", "EUR", new BigDecimal(BigInteger.TEN));

        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        post("/currency/convert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(10.00))
                .andDo(print());
    }

    @Test
    void testDeleteCurrencyReturns200() throws Exception {
        doNothing().when(currencyService).delete(anyLong());

        mockMvc.perform(
                        delete("/currency/{id}", Mockito.anyLong())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testCreateCurrencyReturns400() throws Exception {
        CurrencyRequest request = CurrencyRequest.builder()
                .description("Dollars")
                .build();

        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}
