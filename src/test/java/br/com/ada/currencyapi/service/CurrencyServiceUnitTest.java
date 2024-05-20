package br.com.ada.currencyapi.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.ada.currencyapi.client.AwesomeClient;
import br.com.ada.currencyapi.domain.ConvertCurrencyRequest;
import br.com.ada.currencyapi.domain.ConvertCurrencyResponse;
import br.com.ada.currencyapi.domain.Currency;
import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.domain.ResponseAwesomeClient;
import br.com.ada.currencyapi.exception.CoinNotFoundException;
import br.com.ada.currencyapi.exception.CurrencyException;
import br.com.ada.currencyapi.repository.CurrencyRepository;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceUnitTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private AwesomeClient awesomeClient;

    @Test
    void testGetCurrenciesReturnsListOfCurrencies() {
        // Configuração do Mock do Repository
        List<Currency> list = new ArrayList<>();
        list.add(Currency.builder()
                .id(1L)
                .name("LCS")
                .description("Moeda do lucas")
                .build());
        list.add(Currency.builder()
                .id(2L)
                .name("YAS")
                .description("Moeda da yasmin")
                .build());
        when(currencyRepository.findAll()).thenReturn(list);

        // Execução do método e verificação
        List<CurrencyResponse> responses = currencyService.get();
        assertThat(responses).isNotNull()
                .hasSize(2);
        assertThat(responses.get(0).getLabel()).isEqualTo("1 - LCS");
        assertThat(responses.get(1).getLabel()).isEqualTo("2 - YAS");
        verify(currencyRepository, times(1)).findAll();
    }

    @Test
    void testCreateCurrencyReturnsIdCurrency() {
        // Configuração do Mock do Repository
        when(currencyRepository.findByName(anyString())).thenReturn(null);
        when(currencyRepository.save(any(Currency.class))).thenReturn(Currency.builder().id(3L).build());

        // Execução do método e verificação
        Long id = currencyService.create(CurrencyRequest.builder().name("name").build());
        assertThat(id).isNotNull().isEqualTo(3L);
        verify(currencyRepository, times(1)).findByName(anyString());
        verify(currencyRepository, times(1)).save(any(Currency.class));
    }

    @Test
    void testCreateCurrencyWhenCurrencyAlreadyExistsThrowCurrencyException() {
        // Configuração do Mock do Repository
        when(currencyRepository.findByName(any())).thenReturn(Currency.builder().build());

        // Execução do método e verificação da exceção
        CurrencyException exception = catchThrowableOfType(
                () -> currencyService.create(CurrencyRequest.builder().build()),
                CurrencyException.class);
        verify(currencyRepository, times(1)).findByName(any());
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Coin already exists");
    }

    @Test
    void testDeleteCurrency() {
        // Configuração do Mock do Repository
        doNothing().when(currencyRepository).deleteById(anyLong());

        // Execução do método e verificação
        assertThatCode(() -> currencyService.delete(1L)).doesNotThrowAnyException();
        verify(currencyRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    void testConvertCurrencyWhenExchangeRateSavedInDatabasedReturnsAmount() {
        // Configuração do Mock do Repository
        when(currencyRepository.findByName(any())).thenReturn(
                Currency.builder()
                        .exchanges(Map.of("EUR", BigDecimal.TWO))
                        .build());

        // Execução do método e verificação
        ConvertCurrencyRequest request = ConvertCurrencyRequest
                .builder()
                .to("EUR")
                .amount(BigDecimal.TEN)
                .build();
        ConvertCurrencyResponse response = currencyService.convert(request);
        verify(awesomeClient, never()).get(anyString());
        assertThat(response.getAmount()).isEqualTo(new BigDecimal("20"));
    }

    @Test
    void testConvertCurrencyReturnsAmountUsingApi() {
        // Configuração do Mock do Repository
        when(currencyRepository.findByName(any())).thenReturn(null);
        ResponseEntity<Map<String, ResponseAwesomeClient>> responseAwesome =
                new ResponseEntity<>(Map.of("EURUSD",
                        ResponseAwesomeClient.builder().high("5.00").build()), HttpStatus.OK);
        when(awesomeClient.get(anyString())).thenReturn(responseAwesome);

        // Execução do método e verificação
        ConvertCurrencyRequest request = ConvertCurrencyRequest
                .builder()
                .from("EUR")
                .to("USD")
                .amount(BigDecimal.TWO)
                .build();
        ConvertCurrencyResponse response = currencyService.convert(request);
        verify(awesomeClient, times(1)).get(anyString());
        assertThat(response.getAmount()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    void TestConvertCurrencyWhenCurrencyNotFoundThrowCoinNotFoundException() {
        // Configuração do Mock do Repository e do AwesomeClient
        when(currencyRepository.findByName(any())).thenReturn(null);
        String json = "{\"status\":404,\"code\":\"CoinNotExists\",\"message\":\"moeda nao encontrada EU-USD\"}";
        when(awesomeClient.get(anyString())).thenThrow(new RuntimeException(json));

        // Execução do método e verificação da exceção
        CoinNotFoundException exception = catchThrowableOfType(
                () -> currencyService.convert(new ConvertCurrencyRequest()),
                CoinNotFoundException.class);

        verify(awesomeClient, times(1)).get(anyString());
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage())
                .isEqualTo("Error while getting response from Awesome API: " + json);
    }
}
