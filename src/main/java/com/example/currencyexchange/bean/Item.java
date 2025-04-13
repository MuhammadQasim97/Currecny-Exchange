package com.example.currencyexchange.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String name;
    private String category;
    private BigDecimal price;
    private int quantity;

    @JsonIgnore
    public boolean isGrocery() {
        return "GROCERY".equalsIgnoreCase(category);
    }
    @JsonIgnore
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}