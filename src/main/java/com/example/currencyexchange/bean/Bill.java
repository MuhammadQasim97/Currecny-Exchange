package com.example.currencyexchange.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private List<Item> items;
    private UserType userType;
    private int customerTenureYears;
    private String originalCurrency;
    private String targetCurrency;
    
    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getGroceryAmount() {
        return items.stream()
                .filter(Item::isGrocery)
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getNonGroceryAmount() {
        return items.stream()
                .filter(item -> !item.isGrocery())
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}