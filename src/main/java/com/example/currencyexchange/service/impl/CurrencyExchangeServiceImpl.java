package com.example.currencyexchange.service.impl;

import com.example.currencyexchange.service.CurrencyExchangeService;
import com.example.currencyexchange.exception.ExchangeRateException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String apiUrl;

    public CurrencyExchangeServiceImpl(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${exchange.api.key}") String apiKey,
            @Value("${exchange.api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    @Override
    @Cacheable(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            String url = apiUrl.replace("{base_currency}", fromCurrency) + "?apikey=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode root = objectMapper.readTree(response);
            
            if (!root.path("result").asText().equals("success")) {
                throw new ExchangeRateException("Failed to get exchange rate: " + root.path("error-type").asText());
            }
            
            JsonNode rates = root.path("rates");
            if (!rates.has(toCurrency)) {
                throw new ExchangeRateException("Target currency not found: " + toCurrency);
            }
            
            return new BigDecimal(rates.path(toCurrency).asText()).setScale(6, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ExchangeRateException("Error fetching exchange rate: " + e.getMessage(), e);
        }
    }
}