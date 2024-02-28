package com.purchase.adapter.gateway.repository.purchase;

import com.purchase.domain.usecase.purchase.create.CreatePurchaseInput;
import com.purchase.domain.entity.Purchase;
import com.purchase.domain.interfaces.dataprovider.purchase.PurchaseDataProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PurchaseDataProviderImpl implements PurchaseDataProvider {

    @Autowired
    private PurchaseRepository purchaseRepository; // Autowired instance of the PurchaseRepository

    /**
     * Retrieves a purchase by its ID.
     *
     * @param id The ID of the purchase to retrieve.
     * @return The purchase object if found, otherwise null.
     */
    @Override
    public Purchase getPurchaseById(UUID id) {
        return purchaseRepository.findById(id).orElse(null); // Retrieves a purchase by its ID from the repository
    }

    /**
     * Creates or updates a purchase using the provided input.
     *
     * @param createPurchaseInput The input data for creating or updating the purchase.
     * @return The created or updated purchase object.
     */
    @Override
    public Purchase createPurchase(CreatePurchaseInput createPurchaseInput) {
        Purchase purchase = new Purchase(); // Create a new Purchase object
        BeanUtils.copyProperties(createPurchaseInput, purchase); // Copy properties from the input to the Purchase object
        return purchaseRepository.save(purchase); // Save the purchase object to the repository
    }
}
