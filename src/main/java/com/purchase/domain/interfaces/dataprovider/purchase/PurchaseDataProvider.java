package com.purchase.domain.interfaces.dataprovider.purchase;

import com.purchase.domain.entity.Purchase;
import com.purchase.domain.usecase.purchase.create.CreatePurchaseInput;

import java.util.UUID;

public interface PurchaseDataProvider {
    /**
     * Retrieves a purchase by its ID.
     *
     * @param id The ID of the purchase to retrieve.
     * @return The purchase object if found, otherwise null.
     */
    Purchase getPurchaseById(UUID id);

    /**
     * Creates or updates a purchase using the provided input.
     *
     * @param createPurchaseInput The input data for creating or updating the purchase.
     * @return The created or updated purchase object.
     */
    Purchase createPurchase(CreatePurchaseInput createPurchaseInput);
}
