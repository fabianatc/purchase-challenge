package com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.service;

import com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.client.FiscalDataTreasuryRateExchangeClient;
import com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.dto.RateExchangeOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FiscalDataTreasuryRateExchangeService {

    private static String LOG_PREFIX = "[TREASURY RATE EXCHANGE SERVICE] - ";

    @Autowired
    private FiscalDataTreasuryRateExchangeClient client; // Autowired instance of the Feign client

    /**
     * Retrieves rate exchange data from the Treasury API.
     *
     * @param country           The country for which rate exchange data is requested.
     * @param record_date_limit The date limit for rate exchange records.
     * @return RateExchangeOutput containing rate exchange data.
     */
    public RateExchangeOutput getTreasureRate(String country, String record_date_limit) {
        try {
            // Constructing the filter parameter for the API request
            String filter = "country:eq:" + country + ",record_date:gt:" + record_date_limit;

            log.info("{}Fetching rate exchange data for country: {} with record date limit: {}", LOG_PREFIX, country, record_date_limit);

            // Making a request to the Treasury API through the Feign client
            ResponseEntity<RateExchangeOutput> response = client.getRateExchange("json", filter, "-effective_date");

            // Extracting the response body which contains rate exchange data
            return response != null ? response.getBody() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
