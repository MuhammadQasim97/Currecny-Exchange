package com.example.currencyexchange.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationResult {
    private BigDecimal originalAmount;
    private String originalCurrency;
    private BigDecimal discountedAmount;
    private BigDecimal finalAmount;
    private String targetCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal totalDiscount;
}