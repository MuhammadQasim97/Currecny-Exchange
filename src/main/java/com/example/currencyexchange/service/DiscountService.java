package com.example.currencyexchange.service;

import com.example.currencyexchange.bean.Bill;

import java.math.BigDecimal;

public interface DiscountService {
    BigDecimal calculateDiscount(Bill bill);
}