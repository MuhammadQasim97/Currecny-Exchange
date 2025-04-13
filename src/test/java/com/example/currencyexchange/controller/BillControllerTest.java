package com.example.currencyexchange.controller;

import com.example.currencyexchange.bean.BillRequest;
import com.example.currencyexchange.bean.Bill;
import com.example.currencyexchange.bean.CalculationResult;
import com.example.currencyexchange.bean.Item;
import com.example.currencyexchange.bean.UserType;
import com.example.currencyexchange.service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(billController)
                .build();
    }

    @Test
    void shouldCalculateBill() throws Exception {
        Item item = new Item("Test Item", "ELECTRONICS", new BigDecimal("100"), 1);
        BillRequest request = new BillRequest(
                Collections.singletonList(item),
                UserType.EMPLOYEE,
                0,
                "USD",
                "EUR"
        );
        CalculationResult result = CalculationResult.builder()
                .originalAmount(new BigDecimal("100"))
                .originalCurrency("USD")
                .discountedAmount(new BigDecimal("65.00"))
                .finalAmount(new BigDecimal("55.25"))
                .targetCurrency("EUR")
                .exchangeRate(new BigDecimal("0.85"))
                .totalDiscount(new BigDecimal("35.00"))
                .build();
        when(billService.calculateFinalAmount(any(Bill.class))).thenReturn(result);
        MvcResult mvcResult = mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalAmount").value(100))
                .andExpect(jsonPath("$.originalCurrency").value("USD"))
                .andExpect(jsonPath("$.discountedAmount").value(65.00))
                .andExpect(jsonPath("$.finalAmount").value(55.25))
                .andExpect(jsonPath("$.targetCurrency").value("EUR"))
                .andExpect(jsonPath("$.exchangeRate").value(0.85))
                .andExpect(jsonPath("$.totalDiscount").value(35.00))
                .andReturn();
    }
}