package com.currencycloud.transactbench.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionConfigPropertiesTest {

    private TransactionConfigProperties configProperties;
    private Map<String, Map<String, ProviderConfig>> providers;

    @BeforeEach
    void setUp() {
        configProperties = new TransactionConfigProperties();
        providers = new HashMap<>();
        configProperties.setProviders(providers);
    }

    @Test
    void getConfig_ShouldReturnConfig_WhenProviderAndPaymentRailExist() {
        // Given
        ProviderConfig config = createProviderConfig("EUR", "DE89370400440532013000", "Sender Name", "DE");
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", config);
        providers.put("provider1", paymentRails);

        // When
        ProviderConfig result = configProperties.getConfig("provider1", "sepa");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCurrency()).isEqualTo("EUR");
        assertThat(result.getDestinationAccountNumber()).isEqualTo("DE89370400440532013000");
        assertThat(result.getSenderName()).isEqualTo("Sender Name");
        assertThat(result.getCountry()).isEqualTo("DE");
    }

    @Test
    void getConfig_ShouldThrowException_WhenProviderNotFound() {
        // Given
        // providers map is empty

        // When & Then
        assertThatThrownBy(() -> configProperties.getConfig("invalid-provider", "sepa"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider not found: invalid-provider");
    }

    @Test
    void getConfig_ShouldThrowException_WhenPaymentRailNotFound() {
        // Given
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", createProviderConfig("EUR", "DE89", "Sender", "DE"));
        providers.put("provider1", paymentRails);

        // When & Then
        assertThatThrownBy(() -> configProperties.getConfig("provider1", "swift"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment rail not found: swift for provider: provider1");
    }

    @Test
    void getConfig_ShouldHandleMultipleProviders() {
        // Given
        ProviderConfig sepaConfig = createProviderConfig("EUR", "DE89", "Sender1", "DE");
        ProviderConfig swiftConfig = createProviderConfig("USD", "US12", "Sender2", "US");

        Map<String, ProviderConfig> provider1Rails = new HashMap<>();
        provider1Rails.put("sepa", sepaConfig);
        provider1Rails.put("swift", swiftConfig);
        providers.put("provider1", provider1Rails);

        Map<String, ProviderConfig> provider2Rails = new HashMap<>();
        ProviderConfig provider2Config = createProviderConfig("GBP", "GB34", "Sender3", "GB");
        provider2Rails.put("faster-payments", provider2Config);
        providers.put("provider2", provider2Rails);

        // When
        ProviderConfig result1 = configProperties.getConfig("provider1", "sepa");
        ProviderConfig result2 = configProperties.getConfig("provider1", "swift");
        ProviderConfig result3 = configProperties.getConfig("provider2", "faster-payments");

        // Then
        assertThat(result1.getCurrency()).isEqualTo("EUR");
        assertThat(result2.getCurrency()).isEqualTo("USD");
        assertThat(result3.getCurrency()).isEqualTo("GBP");
    }

    @Test
    void getConfig_ShouldHandleMultiplePaymentRailsForSameProvider() {
        // Given
        ProviderConfig sepaConfig = createProviderConfig("EUR", "DE89", "Sender1", "DE");
        ProviderConfig swiftConfig = createProviderConfig("USD", "US12", "Sender2", "US");
        ProviderConfig targetConfig = createProviderConfig("GBP", "GB34", "Sender3", "GB");

        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", sepaConfig);
        paymentRails.put("swift", swiftConfig);
        paymentRails.put("target2", targetConfig);
        providers.put("provider1", paymentRails);

        // When
        ProviderConfig sepaResult = configProperties.getConfig("provider1", "sepa");
        ProviderConfig swiftResult = configProperties.getConfig("provider1", "swift");
        ProviderConfig targetResult = configProperties.getConfig("provider1", "target2");

        // Then
        assertThat(sepaResult).isSameAs(sepaConfig);
        assertThat(swiftResult).isSameAs(swiftConfig);
        assertThat(targetResult).isSameAs(targetConfig);
    }

    @Test
    void setProviders_ShouldSetProvidersMap() {
        // Given
        Map<String, Map<String, ProviderConfig>> newProviders = new HashMap<>();
        ProviderConfig config = createProviderConfig("EUR", "DE89", "Sender", "DE");
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", config);
        newProviders.put("provider1", paymentRails);

        // When
        configProperties.setProviders(newProviders);

        // Then
        assertThat(configProperties.getProviders()).isSameAs(newProviders);
        assertThat(configProperties.getConfig("provider1", "sepa")).isSameAs(config);
    }

    @Test
    void getProviders_ShouldReturnProvidersMap() {
        // Given
        ProviderConfig config = createProviderConfig("EUR", "DE89", "Sender", "DE");
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", config);
        providers.put("provider1", paymentRails);

        // When
        Map<String, Map<String, ProviderConfig>> result = configProperties.getProviders();

        // Then
        assertThat(result).isSameAs(providers);
        assertThat(result.get("provider1").get("sepa")).isSameAs(config);
    }

    @Test
    void getConfig_ShouldThrowException_WhenProvidersMapIsNull() {
        // Given
        configProperties.setProviders(null);

        // When & Then
        assertThatThrownBy(() -> configProperties.getConfig("provider1", "sepa"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getConfig_ShouldThrowException_WhenPaymentRailsMapIsNull() {
        // Given
        providers.put("provider1", null);

        // When & Then
        assertThatThrownBy(() -> configProperties.getConfig("provider1", "sepa"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getConfig_ShouldHandleCaseSensitiveProviderNames() {
        // Given
        ProviderConfig config = createProviderConfig("EUR", "DE89", "Sender", "DE");
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", config);
        providers.put("Provider1", paymentRails);

        // When & Then
        // Should fail because provider names are case-sensitive
        assertThatThrownBy(() -> configProperties.getConfig("provider1", "sepa"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider not found: provider1");

        // But should work with correct case
        ProviderConfig result = configProperties.getConfig("Provider1", "sepa");
        assertThat(result).isSameAs(config);
    }

    @Test
    void getConfig_ShouldHandleCaseSensitivePaymentRailNames() {
        // Given
        ProviderConfig config = createProviderConfig("EUR", "DE89", "Sender", "DE");
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("SEPA", config);
        providers.put("provider1", paymentRails);

        // When & Then
        // Should fail because payment rail names are case-sensitive
        assertThatThrownBy(() -> configProperties.getConfig("provider1", "sepa"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment rail not found: sepa for provider: provider1");

        // But should work with correct case
        ProviderConfig result = configProperties.getConfig("provider1", "SEPA");
        assertThat(result).isSameAs(config);
    }

    @Test
    void getConfig_ShouldReturnSameInstanceOnMultipleCalls() {
        // Given
        ProviderConfig config = createProviderConfig("EUR", "DE89", "Sender", "DE");
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", config);
        providers.put("provider1", paymentRails);

        // When
        ProviderConfig result1 = configProperties.getConfig("provider1", "sepa");
        ProviderConfig result2 = configProperties.getConfig("provider1", "sepa");

        // Then
        assertThat(result1).isSameAs(result2);
    }

    @Test
    void getConfig_ShouldHandleEmptyPaymentRailsMap() {
        // Given
        providers.put("provider1", new HashMap<>());

        // When & Then
        assertThatThrownBy(() -> configProperties.getConfig("provider1", "sepa"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment rail not found: sepa for provider: provider1");
    }

    @Test
    void getConfig_ShouldReturnConfigWithAllFields() {
        // Given
        ProviderConfig config = createProviderConfig("EUR", "DE89370400440532013000", "Test Sender", "DE");
        Map<String, ProviderConfig> paymentRails = new HashMap<>();
        paymentRails.put("sepa", config);
        providers.put("provider1", paymentRails);

        // When
        ProviderConfig result = configProperties.getConfig("provider1", "sepa");

        // Then
        assertThat(result.getCurrency()).isEqualTo("EUR");
        assertThat(result.getDestinationAccountNumber()).isEqualTo("DE89370400440532013000");
        assertThat(result.getSenderName()).isEqualTo("Test Sender");
        assertThat(result.getCountry()).isEqualTo("DE");
    }

    private ProviderConfig createProviderConfig(String currency, String accountNumber,
                                                 String senderName, String country) {
        ProviderConfig config = new ProviderConfig();
        config.setCurrency(currency);
        config.setDestinationAccountNumber(accountNumber);
        config.setSenderName(senderName);
        config.setCountry(country);
        return config;
    }
}
