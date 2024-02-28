package com.purchase.adapter.controller.purchase;

import com.purchase.domain.usecase.purchase.get.purchaseCountryCurrency.GetPurchaseCountryCurrencyUsecase;
import com.purchase.domain.entity.Purchase;
import com.purchase.domain.usecase.purchase.create.CreatePurchaseInput;
import com.purchase.domain.usecase.purchase.create.CreatePurchaseUsecase;
import com.purchase.domain.usecase.purchase.get.purchaseCountryCurrency.GetPurchaseCountryCurrencyOutput;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/purchase")
public class PurchaseControllerImpl implements PurchaseController {

    @Autowired
    private CreatePurchaseUsecase createPurchase; // Autowired instance of the CreatePurchaseUsecase

    @Autowired
    private GetPurchaseCountryCurrencyUsecase getPurchaseCountryCurrencyUsecase; // Autowired instance of the GetPurchaseCountryCurrencyUsecase

    /**
     * Endpoint implementation for creating a new purchase.
     *
     * @param item The input data for creating the purchase.
     * @return ResponseEntity with the created Purchase object.
     */
    @Override
    public ResponseEntity<Purchase> createPurchase(@Valid @RequestBody CreatePurchaseInput item) {
        return createPurchase.execute(item); // Delegate execution to the CreatePurchaseUsecase
    }

    /**
     * Endpoint implementation for retrieving purchase country currency information.
     *
     * @param purchaseId The ID of the purchase.
     * @param country    The country for currency conversion.
     * @return ResponseEntity with the output data.
     */
    @Override
    public ResponseEntity<GetPurchaseCountryCurrencyOutput> getPurchaseCountryCurrency(@Valid @RequestParam UUID purchaseId, @Valid @RequestParam String country) {
        return getPurchaseCountryCurrencyUsecase.execute(purchaseId, country); // Delegate execution to the GetPurchaseCountryCurrencyUsecase
    }
}
