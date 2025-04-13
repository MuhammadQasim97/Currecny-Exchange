package com.example.currencyexchange.service;

import com.example.currencyexchange.bean.Bill;
import com.example.currencyexchange.bean.CalculationResult;

public interface BillService {
    CalculationResult calculateFinalAmount(Bill bill);
}