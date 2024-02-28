package com.wex.wexpurchase.adapter.gateway.integration.fiscalDataTreasuryGov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateExchangeOutput {
    private List<Data> data; // List of rate exchange data

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Data {
        private double exchange_rate; // Exchange rate value
    }
}
