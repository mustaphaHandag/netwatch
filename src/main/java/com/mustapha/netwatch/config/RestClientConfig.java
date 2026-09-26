package com.mustapha.netwatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * A bare `new RestTemplate()` has no timeout: one unreachable monitored
 * service would hang its check thread forever. Timeouts here bound that,
 * and per-service scheduling (see PerServiceScheduler) means a hang on one
 * service's thread never delays the others anyway.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                      @Value("${netwatch.http-client.connect-timeout-ms:5000}") long connectTimeoutMs,
                                      @Value("${netwatch.http-client.read-timeout-ms:5000}") long readTimeoutMs) {
        return builder
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .readTimeout(Duration.ofMillis(readTimeoutMs))
                .build();
    }
}
