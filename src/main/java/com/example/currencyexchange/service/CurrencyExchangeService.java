package com.example.currencyexchange.service;

import java.math.BigDecimal;

public interface CurrencyExchangeService {
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);
}