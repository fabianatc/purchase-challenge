package com.wex.wexpurchase.domain.usecase.purchase.create;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreatePurchaseInput {
    @Size(max = 50, message = "Description must not exceed 50 characters")
    @NotBlank(message = "Description is required")
    private String description; // Description of the purchase

    @NotNull(message = "Purchase date is required")
    @PastOrPresent(message = "Purchase date must be in the past or present")
    private LocalDate purchaseDate; // Date of the purchase

    @NotNull(message = "Purchase amount is required")
    @Positive(message = "Purchase amount must be positive")
    @Digits(integer = 12, fraction = 2, message = "Purchase amount must have a maximum of 12 digits with 2 decimal places")
    private BigDecimal dolarPrice; // Price in dolar of the purchase
}
