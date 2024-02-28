package com.wex.wexpurchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WexPurchaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WexPurchaseApplication.class, args);
    }

}
