package com.purchase.domain.usecase.purchase.get.purchaseCountryCurrency;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class GetPurchaseCountryCurrencyOutput {
    private UUID purchaseId; // The ID of the purchase
    private String description; // The description of the purchase
    private LocalDate purchaseDate; // The date of the purchase
    private BigDecimal dolarPrice; // The price in dolar of the purchase
    private BigDecimal rate; // The currency exchange rate
    private BigDecimal convertedPrice; // The price converted to the target currency
}
