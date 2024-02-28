package com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.client;

import com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.dto.RateExchangeOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "treasury", url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/")
public interface FiscalDataTreasuryRateExchangeClient {

    /**
     * Fetches rate exchange data from the Treasury API.
     *
     * @param format The response format.
     * @param filter The filter parameters for the request.
     * @param sort   The sorting criteria for the response.
     * @return ResponseEntity containing the RateExchangeOutput object.
     */
    @GetMapping("v1/accounting/od/rates_of_exchange")
    ResponseEntity<RateExchangeOutput> getRateExchange(
            @RequestParam("format") String format,
            @RequestParam("filter") String filter,
            @RequestParam("sort") String sort
    );
}
