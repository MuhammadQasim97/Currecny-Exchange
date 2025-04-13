package com.example.currencyexchange.service;

import com.example.currencyexchange.exception.ExchangeRateException;
import com.example.currencyexchange.service.CurrencyExchangeService;
import com.example.currencyexchange.service.impl.CurrencyExchangeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private CurrencyExchangeService currencyExchangeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        currencyExchangeService = new CurrencyExchangeServiceImpl(
                restTemplate,
                objectMapper,
                "dummy-api-key",
                "https://api.example.com/v1/latest/{base_currency}"
        );
    }

    @Test
    void shouldReturnCorrectExchangeRate() {
        // Given
        String successResponse = "{"
                + "\"result\": \"success\","
                + "\"base_code\": \"USD\","
                + "\"rates\": {"
                + "\"EUR\": \"0.85\","
                + "\"GBP\": \"0.75\""
                + "}"
                + "}";
        
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(successResponse);
        BigDecimal rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(new BigDecimal("0.850000"), rate);
    }

    @Test
    void shouldThrowExceptionWhenApiCallFails() {
        String errorResponse = "{"
                + "\"result\": \"error\","
                + "\"error-type\": \"invalid-key\""
                + "}";
        
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(errorResponse);
        assertThrows(ExchangeRateException.class, () ->
                currencyExchangeService.getExchangeRate("USD", "EUR"));
    }

    @Test
    void shouldThrowExceptionWhenTargetCurrencyNotFound() {
        String successResponse = "{"
                + "\"result\": \"success\","
                + "\"base_code\": \"USD\","
                + "\"rates\": {"
                + "\"EUR\": \"0.85\","
                + "\"GBP\": \"0.75\""
                + "}"
                + "}";
        
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(successResponse);
        assertThrows(ExchangeRateException.class, () ->
                currencyExchangeService.getExchangeRate("USD", "JPY"));
    }
}