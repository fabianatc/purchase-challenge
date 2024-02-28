package com.purchase.adapter.controller.purchase;

import com.purchase.domain.entity.Purchase;
import com.purchase.domain.usecase.purchase.create.CreatePurchaseInput;
import com.purchase.domain.usecase.purchase.get.purchaseCountryCurrency.GetPurchaseCountryCurrencyOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/purchase")
public interface PurchaseController {

    /**
     * Endpoint for creating a new purchase.
     *
     * @param item The input data for creating the purchase.
     * @return ResponseEntity with the created Purchase object.
     */
    @PostMapping("/")
    ResponseEntity<Purchase> createPurchase(CreatePurchaseInput item);

    /**
     * Endpoint for retrieving purchase country currency information.
     *
     * @param purchaseId The ID of the purchase.
     * @param country    The country for currency conversion.
     * @return ResponseEntity with the output data.
     */
    @GetMapping("/country_currency")
    ResponseEntity<GetPurchaseCountryCurrencyOutput> getPurchaseCountryCurrency(UUID purchaseId, String country);
}
