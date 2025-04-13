package com.example.currencyexchange.service;

import com.example.currencyexchange.bean.Bill;
import com.example.currencyexchange.bean.CalculationResult;
import com.example.currencyexchange.bean.Item;
import com.example.currencyexchange.bean.UserType;
import com.example.currencyexchange.service.impl.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private DiscountService discountService;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    private BillService billService;

    @BeforeEach
    void setUp() {
        billService = new BillServiceImpl(discountService, currencyExchangeService);
    }

    @Test
    void shouldCalculateFinalAmountWithoutCurrencyConversion() {
        // Given
        Item item = new Item("Test Item", "ELECTRONICS", new BigDecimal("100"), 1);
        Bill bill = new Bill(
                Collections.singletonList(item),
                UserType.EMPLOYEE,
                0,
                "USD",
                "USD"
        );
        
        when(discountService.calculateDiscount(any(Bill.class)))
                .thenReturn(new BigDecimal("35.00"));
        CalculationResult result = billService.calculateFinalAmount(bill);
        assertEquals(new BigDecimal("100"), result.getOriginalAmount());
        assertEquals(new BigDecimal("65.00"), result.getDiscountedAmount());
        assertEquals(new BigDecimal("65.00"), result.getFinalAmount());
        assertEquals(BigDecimal.ONE, result.getExchangeRate());
        assertEquals(new BigDecimal("35.00"), result.getTotalDiscount());
    }

    @Test
    void shouldCalculateFinalAmountWithCurrencyConversion() {
        Item item = new Item("Test Item", "ELECTRONICS", new BigDecimal("100"), 1);
        Bill bill = new Bill(
                Collections.singletonList(item),
                UserType.EMPLOYEE,
                0,
                "USD",
                "EUR"
        );
        
        when(discountService.calculateDiscount(any(Bill.class)))
                .thenReturn(new BigDecimal("35.00"));
        
        when(currencyExchangeService.getExchangeRate("USD", "EUR"))
                .thenReturn(new BigDecimal("0.85"));
        CalculationResult result = billService.calculateFinalAmount(bill);
        assertEquals(new BigDecimal("100"), result.getOriginalAmount());
        assertEquals(new BigDecimal("65.00"), result.getDiscountedAmount());
        assertEquals(new BigDecimal("55.25"), result.getFinalAmount());
        assertEquals(new BigDecimal("0.85"), result.getExchangeRate());
        assertEquals(new BigDecimal("35.00"), result.getTotalDiscount());
    }
}