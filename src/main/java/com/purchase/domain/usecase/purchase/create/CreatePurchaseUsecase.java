package com.purchase.domain.usecase.purchase.create;

import com.purchase.domain.entity.Purchase;
import com.purchase.domain.interfaces.dataprovider.purchase.PurchaseDataProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@AllArgsConstructor
public class CreatePurchaseUsecase {
    private static final String LOG_PREFIX = "[CREATE PURCHASE USECASE] - ";

    @Autowired
    private PurchaseDataProvider purchaseDataProvider;

    /**
     * Executes the use case to create a new purchase.
     *
     * @param item The input data for creating the purchase.
     * @return ResponseEntity with the created Purchase object.
     */
    public ResponseEntity<Purchase> execute(CreatePurchaseInput item) {
        try {
            // Attempt to create the purchase using the provided input
            Purchase createdPurchase = purchaseDataProvider.createPurchase(item);

            // Return a ResponseEntity with the created purchase and HTTP status CREATED (201)
            return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error and throw an internal server error with a generic error message
            log.error("{}Some error occurred while creating the purchase: {}", LOG_PREFIX, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some error occurred while creating the purchase");
        }
    }
}