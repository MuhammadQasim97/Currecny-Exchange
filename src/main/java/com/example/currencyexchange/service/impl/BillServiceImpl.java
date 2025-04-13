package com.example.currencyexchange.service.impl;

import com.example.currencyexchange.bean.Bill;
import com.example.currencyexchange.bean.CalculationResult;
import com.example.currencyexchange.service.BillService;
import com.example.currencyexchange.service.CurrencyExchangeService;
import com.example.currencyexchange.service.DiscountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BillServiceImpl implements BillService {

    private final DiscountService discountService;
    private final CurrencyExchangeService currencyExchangeService;

    public BillServiceImpl(DiscountService discountService, CurrencyExchangeService currencyExchangeService) {
        this.discountService = discountService;
        this.currencyExchangeService = currencyExchangeService;
    }

    @Override
    public CalculationResult calculateFinalAmount(Bill bill) {
        BigDecimal originalAmount = bill.getTotalAmount();
        BigDecimal totalDiscount = discountService.calculateDiscount(bill);
        BigDecimal discountedAmount = originalAmount.subtract(totalDiscount);
        
        if (bill.getOriginalCurrency().equals(bill.getTargetCurrency())) {
            return CalculationResult.builder()
                    .originalAmount(originalAmount)
                    .originalCurrency(bill.getOriginalCurrency())
                    .discountedAmount(discountedAmount)
                    .finalAmount(discountedAmount)
                    .targetCurrency(bill.getTargetCurrency())
                    .exchangeRate(BigDecimal.ONE)
                    .totalDiscount(totalDiscount)
                    .build();
        }

        BigDecimal exchangeRate = currencyExchangeService.getExchangeRate(
                bill.getOriginalCurrency(), bill.getTargetCurrency());
        
        BigDecimal finalAmount = discountedAmount.multiply(exchangeRate)
                .setScale(2, RoundingMode.HALF_UP);
        
        return CalculationResult.builder()
                .originalAmount(originalAmount)
                .originalCurrency(bill.getOriginalCurrency())
                .discountedAmount(discountedAmount)
                .finalAmount(finalAmount)
                .targetCurrency(bill.getTargetCurrency())
                .exchangeRate(exchangeRate)
                .totalDiscount(totalDiscount)
                .build();
    }
}