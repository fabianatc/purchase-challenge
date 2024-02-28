package com.wex.wexpurchase.application.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class FeignClientRequestInterceptor implements RequestInterceptor {

    /**
     * Applies custom logic to the outgoing request template.
     *
     * @param template The request template to be modified.
     */
    @Override
    public void apply(RequestTemplate template) {
        try {
            // Decode the request URL from URL encoding
            String decodedUrl = URLDecoder.decode(template.request().url(), StandardCharsets.UTF_8.toString());
            // Set the URI of the request template to the decoded URL
            template.uri(decodedUrl);
            // Log the decoded URL
            log.info("Outgoing request URL: {}", decodedUrl);
        } catch (UnsupportedEncodingException e) {
            // If decoding fails, throw a RuntimeException
            throw new RuntimeException(e);
        }
    }
}

