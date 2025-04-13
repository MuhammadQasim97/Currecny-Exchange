package com.example.currencyexchange.service;

import com.example.currencyexchange.bean.Bill;
import com.example.currencyexchange.bean.Item;
import com.example.currencyexchange.bean.UserType;
import com.example.currencyexchange.service.impl.DiscountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountServiceTest {

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountServiceImpl();
    }

    @Test
    void shouldApplyEmployeeDiscount() {
        // Given
        Item item = new Item("Non-Grocery Item", "ELECTRONICS", new BigDecimal("100"), 1);
        Bill bill = new Bill(
                Collections.singletonList(item),
                UserType.EMPLOYEE,
                0,
                "USD",
                "USD"
        );

        // When
        BigDecimal discount = discountService.calculateDiscount(bill);

        // Then
        // 30% of $100 = $30 (percentage discount) + $5 (amount discount) = $35
        assertEquals(new BigDecimal("35.00"), discount);
    }

    @Test
    void shouldApplyAffiliateDiscount() {
        // Given
        Item item = new Item("Non-Grocery Item", "ELECTRONICS", new BigDecimal("100"), 1);
        Bill bill = new Bill(
                Collections.singletonList(item),
                UserType.AFFILIATE,
                0,
                "USD",
                "USD"
        );

        // When
        BigDecimal discount = discountService.calculateDiscount(bill);

        // Then
        // 10% of $100 = $10 (percentage discount) + $5 (amount discount) = $15
        assertEquals(new BigDecimal("15.00"), discount);
    }

    @Test
    void shouldApplyLoyalCustomerDiscount() {
        // Given
        Item item = new Item("Non-Grocery Item", "ELECTRONICS", new BigDecimal("100"), 1);
        Bill bill = new Bill(
                Collections.singletonList(item),
                UserType.CUSTOMER,
                3, // More than 2 years
                "USD",
                "USD"
        );

        // When
        BigDecimal discount = discountService.calculateDiscount(bill);

        // Then
        // 5% of $100 = $5 (percentage discount) + $5 (amount discount) = $10
        assertEquals(new BigDecimal("10.00"), discount);
    }

    @Test
    void shouldNotApplyPercentageDiscountToGroceries() {
        // Given
        Item groceryItem = new Item("Grocery Item", "GROCERY", new BigDecimal("100"), 1);
        Bill bill = new Bill(
                Collections.singletonList(groceryItem),
                UserType.EMPLOYEE,
                0,
                "USD",
                "USD"
        );
        BigDecimal discount = discountService.calculateDiscount(bill);

        assertEquals(new BigDecimal("5.00"), discount);
    }

    @Test
    void shouldApplyAmountDiscountToEntireBill() {
        Item groceryItem = new Item("Grocery Item", "GROCERY", new BigDecimal("100"), 1);
        Item nonGroceryItem = new Item("Non-Grocery Item", "ELECTRONICS", new BigDecimal("100"), 1);
        Bill bill = new Bill(
                Arrays.asList(groceryItem, nonGroceryItem),
                UserType.EMPLOYEE,
                0,
                "USD",
                "USD"
        );
        BigDecimal discount = discountService.calculateDiscount(bill);
        assertEquals(new BigDecimal("40.00"), discount);
    }
}