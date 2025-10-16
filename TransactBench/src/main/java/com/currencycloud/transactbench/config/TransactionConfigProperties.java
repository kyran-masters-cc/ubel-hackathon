package com.currencycloud.transactbench.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "transaction")
@Data
public class TransactionConfigProperties {
    private Map<String, Map<String, ProviderConfig>> providers;

    public ProviderConfig getConfig(String provider, String paymentRail) {
        Map<String, ProviderConfig> providerMap = providers.get(provider);
        if (providerMap == null) {
            throw new IllegalArgumentException("Provider not found: " + provider);
        }
        ProviderConfig config = providerMap.get(paymentRail);
        if (config == null) {
            throw new IllegalArgumentException(
                "Payment rail not found: " + paymentRail + " for provider: " + provider);
        }
        return config;
    }
}