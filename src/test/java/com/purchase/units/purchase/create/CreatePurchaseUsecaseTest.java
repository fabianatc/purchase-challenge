package com.purchase.units.purchase.create;

import com.purchase.domain.usecase.purchase.create.CreatePurchaseInput;
import com.purchase.domain.usecase.purchase.create.CreatePurchaseUsecase;
import com.purchase.domain.entity.Purchase;
import com.purchase.domain.interfaces.dataprovider.purchase.PurchaseDataProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CreatePurchaseUsecaseTest {
    // Mock dependencies
    @Mock
    private PurchaseDataProvider purchaseDataProvider;

    @Test
    @DisplayName("Test create purchase with valid input")
    void testCreatePurchaseWithValidInput() {
        // Create input data
        CreatePurchaseInput input = new CreatePurchaseInput();
        input.setDescription("Valid description");
        input.setPurchaseDate(LocalDate.now());
        input.setDolarPrice(BigDecimal.valueOf(100.00));

        // Create expected purchase object
        Purchase expectedPurchase = new Purchase();
        expectedPurchase.setId(UUID.randomUUID());
        expectedPurchase.setDescription("Valid description");
        expectedPurchase.setPurchaseDate(LocalDate.now());
        expectedPurchase.setDolarPrice(BigDecimal.valueOf(100.00));

        // Set up mock behavior
        when(purchaseDataProvider.createPurchase(input)).thenReturn(expectedPurchase);

        // Create instance of CreatePurchaseUsecase
        CreatePurchaseUsecase usecase = new CreatePurchaseUsecase(purchaseDataProvider);

        // Execute the use case
        ResponseEntity<Purchase> response = usecase.execute(input);

        // Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedPurchase, response.getBody());

        // Verify the mock behavior
        verify(purchaseDataProvider).createPurchase(input);
    }

    @Test
    @DisplayName("Test throw exception on purchase creation error")
    void testThrowExceptionOnPurchaseCreationError() {
        // Create an instance of CreatePurchaseUsecase
        CreatePurchaseUsecase createPurchaseUsecase = new CreatePurchaseUsecase(purchaseDataProvider);

        // Mock the createPurchase method to throw an exception
        CreatePurchaseInput input = new CreatePurchaseInput();
        when(purchaseDataProvider.createPurchase(input)).thenThrow(new RuntimeException("Some error occurred"));

        // Assert that a ResponseStatusException is thrown with the correct status and message
        assertThrows(ResponseStatusException.class, () -> createPurchaseUsecase.execute(input));
    }

    @Test
    @DisplayName("Test return response entity with created purchase")
    void testReturnResponseEntityWithCreatedPurchase() {
        CreatePurchaseInput input = new CreatePurchaseInput();
        Purchase createdPurchase = new Purchase();
        ResponseEntity<Purchase> expectedResponse = new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);

        // Set up mock behavior
        when(purchaseDataProvider.createPurchase(input)).thenReturn(createdPurchase);

        // Create instance of CreatePurchaseUsecase
        CreatePurchaseUsecase usecase = new CreatePurchaseUsecase(purchaseDataProvider);

        // Execute the use case
        ResponseEntity<Purchase> actualResponse = usecase.execute(input);

        // Verify the result
        assertEquals(expectedResponse, actualResponse);
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(createdPurchase, actualResponse.getBody());
    }
}
