package com.example.currencyexchange.controller;

import com.example.currencyexchange.service.BillService;
import com.example.currencyexchange.bean.BillRequest;
import com.example.currencyexchange.bean.Bill;
import com.example.currencyexchange.bean.CalculationResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculationResult> calculateBill(@Valid @RequestBody BillRequest billRequest) {
        Bill bill = new Bill(
                billRequest.getItems(),
                billRequest.getUserType(),
                billRequest.getCustomerTenureYears(),
                billRequest.getOriginalCurrency(),
                billRequest.getTargetCurrency()
        );
        
        CalculationResult result = billService.calculateFinalAmount(bill);
        return ResponseEntity.ok(result);
    }
}