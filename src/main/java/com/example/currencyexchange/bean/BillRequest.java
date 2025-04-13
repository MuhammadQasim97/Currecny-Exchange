package com.example.currencyexchange.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {
    @NotEmpty(message = "Items cannot be empty")
    private List<Item> items;
    
    @NotNull(message = "User type is required")
    private UserType userType;
    
    private int customerTenureYears;
    
    @NotNull(message = "Original currency is required")
    private String originalCurrency;
    
    @NotNull(message = "Target currency is required")
    private String targetCurrency;
}