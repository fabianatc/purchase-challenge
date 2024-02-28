package com.wex.wexpurchase.units.purchase.get;

import com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.dto.RateExchangeOutput;
import com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.service.FiscalDataTreasuryRateExchangeService;
import com.wex.wexpurchase.domain.entity.Purchase;
import com.wex.wexpurchase.domain.interfaces.dataprovider.purchase.PurchaseDataProvider;
import com.wex.wexpurchase.domain.usecase.purchase.get.purchaseCountryCurrency.GetPurchaseCountryCurrencyOutput;
import com.wex.wexpurchase.domain.usecase.purchase.get.purchaseCountryCurrency.GetPurchaseCountryCurrencyUsecase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetPurchaseCountryCurrencyUsecaseTest {
    // Mock dependencies
    @Mock
    private PurchaseDataProvider purchaseDataProvider;

    @Mock
    private FiscalDataTreasuryRateExchangeService fiscalDataTreasuryRateExchangeService;


    @Test
    @DisplayName("Test execute success with valid input and rate exchange data available")
    void testExecuteSuccessWithValidInputAndRateExchangeDataAvailable() {
        // Create instance of GetPurchaseCountryCurrencyUsecase
        GetPurchaseCountryCurrencyUsecase usecase = new GetPurchaseCountryCurrencyUsecase(purchaseDataProvider, fiscalDataTreasuryRateExchangeService);

        // Set up test data
        UUID purchaseId = UUID.randomUUID();
        String country = "Brazil";

        // Set up mock behavior for dependencies
        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setDescription("Test Purchase");
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setDolarPrice(BigDecimal.valueOf(100.00));

        when(purchaseDataProvider.getPurchaseById(purchaseId)).thenReturn(purchase);

        RateExchangeOutput rateExchangeOutput = new RateExchangeOutput();
        RateExchangeOutput.Data rateExchangeData = new RateExchangeOutput.Data();
        rateExchangeData.setExchange_rate(1.5);
        rateExchangeOutput.setData(Collections.singletonList(rateExchangeData));

        when(fiscalDataTreasuryRateExchangeService.getTreasureRate(country, LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).thenReturn(rateExchangeOutput);

        // Execute the use case
        ResponseEntity<GetPurchaseCountryCurrencyOutput> response = usecase.execute(purchaseId, country);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        GetPurchaseCountryCurrencyOutput output = response.getBody();
        assertEquals(purchaseId, output.getPurchaseId());
        assertEquals(purchase.getDescription(), output.getDescription());
        assertEquals(purchase.getPurchaseDate(), output.getPurchaseDate());
        assertEquals(purchase.getDolarPrice(), output.getDolarPrice());
        assertEquals(BigDecimal.valueOf(rateExchangeData.getExchange_rate()), output.getRate());
        assertEquals(purchase.getDolarPrice().multiply(BigDecimal.valueOf(rateExchangeData.getExchange_rate())).setScale(2, RoundingMode.HALF_UP), output.getConvertedPrice());

        // Verify the mock interactions
        verify(purchaseDataProvider).getPurchaseById(purchaseId);
        verify(fiscalDataTreasuryRateExchangeService).getTreasureRate(country, LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    @DisplayName("Test execute throws bad request exception when purchase id or country is null or blank")
    void testExecuteThrowsBadRequestExceptionWhenPurchaseIdOrCountryIsNullOrBlank() {
        // Create instance of GetPurchaseCountryCurrencyUsecase
        GetPurchaseCountryCurrencyUsecase usecase = new GetPurchaseCountryCurrencyUsecase(purchaseDataProvider, fiscalDataTreasuryRateExchangeService);

        // Set up test data
        UUID purchaseId = UUID.randomUUID();
        String country = null;

        // Execute the use case and verify the exception
        assertThrows(ResponseStatusException.class, () -> usecase.execute(purchaseId, country));
    }

    @Test
    @DisplayName("Test rate exchange data not found")
    void testRateExchangeDataNotFound() {
        // Create test data
        UUID purchaseId = UUID.randomUUID();
        String country = "Brazil";
        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(LocalDate.now());

        // Mock the rate exchange output
        RateExchangeOutput output = new RateExchangeOutput();
        output.setData(new ArrayList<>());

        // Define behavior of mocks
        when(purchaseDataProvider.getPurchaseById(purchaseId)).thenReturn(purchase);
        when(fiscalDataTreasuryRateExchangeService.getTreasureRate(
                country,
                purchase.getPurchaseDate().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .thenReturn(output);

        // Create instance of GetPurchaseCountryCurrencyUsecase
        GetPurchaseCountryCurrencyUsecase usecase = new GetPurchaseCountryCurrencyUsecase(purchaseDataProvider, fiscalDataTreasuryRateExchangeService);

        // Assert that an exception is thrown
        assertThrows(ResponseStatusException.class, () -> usecase.execute(purchaseId, country));
    }

    @Test
    @DisplayName("Test execute throws response exception when rate exchange data not found")
    void testExecuteThrowsResponseStatusExceptionWhenRateExchangeDataNotFound() {
        // Mock the purchase
        Purchase purchase = new Purchase();
        purchase.setId(UUID.randomUUID());
        purchase.setPurchaseDate(LocalDate.now());

        // Mock the rate exchange output
        RateExchangeOutput rateExchangeOutput = new RateExchangeOutput();
        rateExchangeOutput.setData(Collections.emptyList());

        // Mock the behavior of the dependencies
        when(purchaseDataProvider.getPurchaseById(purchase.getId())).thenReturn(purchase);
        when(fiscalDataTreasuryRateExchangeService.getTreasureRate(Mockito.anyString(), Mockito.anyString())).thenReturn(rateExchangeOutput);

        // Create the use case
        GetPurchaseCountryCurrencyUsecase usecase = new GetPurchaseCountryCurrencyUsecase(purchaseDataProvider, fiscalDataTreasuryRateExchangeService);

        // Execute the use case and expect a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> usecase.execute(purchase.getId(), "USA"));
    }

    @Test
    @DisplayName("Test valid http status and result")
    void testValidStatusAndResultReturn() {
        // Arrange
        UUID purchaseId = UUID.randomUUID();
        String country = "Brazil";
        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setDolarPrice(BigDecimal.valueOf(500));

        RateExchangeOutput rateExchangeOutput = new RateExchangeOutput();
        rateExchangeOutput.setData(Collections.singletonList(new RateExchangeOutput.Data()));
        GetPurchaseCountryCurrencyOutput expectedOutput = GetPurchaseCountryCurrencyOutput.builder().build();

        GetPurchaseCountryCurrencyUsecase usecase = new GetPurchaseCountryCurrencyUsecase(purchaseDataProvider, fiscalDataTreasuryRateExchangeService);

        when(purchaseDataProvider.getPurchaseById(purchaseId)).thenReturn(purchase);
        when(fiscalDataTreasuryRateExchangeService.getTreasureRate(
                country,
                purchase.getPurchaseDate().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .thenReturn(rateExchangeOutput);

        // Act
        ResponseEntity<GetPurchaseCountryCurrencyOutput> response = usecase.execute(purchaseId, country);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOutput.getPurchaseId(), Objects.requireNonNull(response.getBody()).getPurchaseId());
        verify(purchaseDataProvider, Mockito.times(1)).getPurchaseById(purchaseId);
        verify(fiscalDataTreasuryRateExchangeService,
                Mockito.times(1))
                .getTreasureRate(country, purchase.getPurchaseDate().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
