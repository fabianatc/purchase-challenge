package com.wex.wexpurchase.domain.usecase.purchase.get.purchaseCountryCurrency;

import com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.dto.RateExchangeOutput;
import com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.service.FiscalDataTreasuryRateExchangeService;
import com.wex.wexpurchase.domain.entity.Purchase;
import com.wex.wexpurchase.domain.interfaces.dataprovider.purchase.PurchaseDataProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class GetPurchaseCountryCurrencyUsecase {
    private static final String LOG_PREFIX = "[GET PURCHASE COUNTRY CURRENCY USECASE] - ";

    @Autowired
    private PurchaseDataProvider purchaseDataProvider;

    @Autowired
    private FiscalDataTreasuryRateExchangeService fiscalDataTreasuryRateExchangeService;

    /**
     * Executes the use case to get purchase country currency.
     *
     * @param purchaseId The ID of the purchase.
     * @param country    The country for currency conversion.
     * @return ResponseEntity with the output data.
     */
    public ResponseEntity<GetPurchaseCountryCurrencyOutput> execute(UUID purchaseId, String country) {
        log.info("{}Executing with purchaseId: {} and country: {}", LOG_PREFIX, purchaseId, country);

        validateInput(purchaseId, country); // Validate input parameters

        Purchase purchase = fetchPurchase(purchaseId); // Fetch purchase data

        LocalDate purchaseDate = purchase.getPurchaseDate();
        RateExchangeOutput rateExchangeOutput = fetchRateExchange(country, purchaseDate); // Fetch rate exchange

        if (rateExchangeOutput.getData().isEmpty()) {
            log.warn("{}No rate exchange data found for country: {} and purchase date: {}", LOG_PREFIX, country, purchaseDate);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The purchase cannot be converted to the target currency");
        }

        GetPurchaseCountryCurrencyOutput output = buildOutput(purchase, rateExchangeOutput.getData().get(0));
        log.info("{}Execution completed successfully", LOG_PREFIX);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    /**
     * Validates input parameters.
     *
     * @param purchaseId The ID of the purchase.
     * @param country    The country for currency conversion.
     */
    private void validateInput(UUID purchaseId, String country) {
        if (purchaseId == null || country == null || country.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase ID and country are required");
        }
    }

    /**
     * Fetches the purchase data.
     *
     * @param purchaseId The ID of the purchase.
     * @return The fetched Purchase object.
     */
    private Purchase fetchPurchase(UUID purchaseId) {
        Purchase purchase = purchaseDataProvider.getPurchaseById(purchaseId);
        if (purchase == null) {
            log.warn("{}No purchase found for ID: {}", LOG_PREFIX, purchaseId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The purchase cannot be found");
        }
        return purchase;
    }

    /**
     * Fetches the rate exchange data.
     *
     * @param country      The country for currency conversion.
     * @param purchaseDate The date of the purchase.
     * @return The fetched RateExchangeOutput object.
     */
    private RateExchangeOutput fetchRateExchange(String country, LocalDate purchaseDate) {
        // Calculate the date limit 6 months prior to the purchase date
        LocalDate dateLimit = purchaseDate.minusMonths(6);
        String dateLimitStr = dateLimit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Fetch rate exchange data from the service
        RateExchangeOutput rateExchangeOutput = fiscalDataTreasuryRateExchangeService.getTreasureRate(country, dateLimitStr);

        // Check if rate exchange data is null
        if (rateExchangeOutput == null) {
            // Log an error if rate exchange data is null
            log.error("{}Error occurred while accessing the API", LOG_PREFIX);
            // Throw an internal server error indicating a problem with accessing the API
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some error occurred while converting currency. Try again later.");
        }

        // Check if rate exchange data is empty
        if (rateExchangeOutput.getData().isEmpty()) {
            // Log a warning if rate exchange data is empty
            log.warn("{}No rate exchange data found for country: {} and date limit: {}", LOG_PREFIX, country, dateLimitStr);
            // Throw a not found exception indicating that rate exchange data cannot be found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The rate exchange data cannot be found");
        }

        // Return the fetched rate exchange data
        return rateExchangeOutput;
    }

    /**
     * Builds the output object.
     *
     * @param purchase The Purchase object.
     * @param data     The RateExchangeOutput data.
     * @return The constructed GetPurchaseCountryCurrencyOutput object.
     */
    private GetPurchaseCountryCurrencyOutput buildOutput(Purchase purchase, RateExchangeOutput.Data data) {
        BigDecimal convertedPrice = calculateConvertedPrice(purchase.getDolarPrice(), BigDecimal.valueOf(data.getExchange_rate()));
        return GetPurchaseCountryCurrencyOutput.builder()
                .purchaseId(purchase.getId())
                .rate(BigDecimal.valueOf(data.getExchange_rate()))
                .dolarPrice(purchase.getDolarPrice())
                .description(purchase.getDescription())
                .purchaseDate(purchase.getPurchaseDate())
                .convertedPrice(convertedPrice)
                .build();
    }

    /**
     * Calculates the converted price.
     *
     * @param price The original price.
     * @param rate  The exchange rate.
     * @return The converted price.
     */
    private BigDecimal calculateConvertedPrice(BigDecimal price, BigDecimal rate) {
        return price.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}