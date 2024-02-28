package com.wex.wexpurchase.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Unique identifier for the purchase

    @NotNull
    private String description; // Description of the purchase

    @NotNull
    private LocalDate purchaseDate; // Date of the purchase

    @Column(precision = 12, scale = 2)
    @NotNull
    private BigDecimal dolarPrice; // Price in dolar of the purchase
}
