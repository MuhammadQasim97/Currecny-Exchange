package com.example.currencyexchange.service.impl;

import com.example.currencyexchange.bean.Bill;
import com.example.currencyexchange.service.DiscountService;
import com.example.currencyexchange.bean.UserType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DiscountServiceImpl implements DiscountService {

    private static final BigDecimal EMPLOYEE_DISCOUNT_RATE = new BigDecimal("0.30");
    private static final BigDecimal AFFILIATE_DISCOUNT_RATE = new BigDecimal("0.10");
    private static final BigDecimal LOYAL_CUSTOMER_DISCOUNT_RATE = new BigDecimal("0.05");
    private static final BigDecimal AMOUNT_PER_DISCOUNT = new BigDecimal("100");
    private static final BigDecimal DISCOUNT_PER_AMOUNT = new BigDecimal("5");

    @Override
    public BigDecimal calculateDiscount(Bill bill) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal percentageDiscount = calculatePercentageDiscount(bill);
        totalDiscount = totalDiscount.add(percentageDiscount);
        BigDecimal amountDiscount = calculateAmountDiscount(bill.getTotalAmount());
        totalDiscount = totalDiscount.add(amountDiscount);
        
        return totalDiscount;
    }
    
    private BigDecimal calculatePercentageDiscount(Bill bill) {
        BigDecimal discountRate = getDiscountRate(bill.getUserType(), bill.getCustomerTenureYears());
        return bill.getNonGroceryAmount().multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal getDiscountRate(UserType userType, int customerTenureYears) {
        if (userType == UserType.EMPLOYEE) {
            return EMPLOYEE_DISCOUNT_RATE;
        } else if (userType == UserType.AFFILIATE) {
            return AFFILIATE_DISCOUNT_RATE;
        } else if (customerTenureYears > 2) {
            return LOYAL_CUSTOMER_DISCOUNT_RATE;
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateAmountDiscount(BigDecimal totalAmount) {
        BigDecimal discountUnits = totalAmount.divide(AMOUNT_PER_DISCOUNT, 0, RoundingMode.FLOOR);
        return discountUnits.multiply(DISCOUNT_PER_AMOUNT);
    }
}