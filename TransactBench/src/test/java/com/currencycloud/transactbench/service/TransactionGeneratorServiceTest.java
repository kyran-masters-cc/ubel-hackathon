package com.currencycloud.transactbench.service;

import com.currencycloud.transactbench.config.ProviderConfig;
import com.currencycloud.transactbench.config.TransactionConfigProperties;
import com.currencycloud.transactbench.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionGeneratorServiceTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Mock
    private TransactionConfigProperties configProperties;

    @InjectMocks
    private TransactionGeneratorService transactionGeneratorService;

    private ProviderConfig providerConfig;

    @BeforeEach
    void setUp() {
        providerConfig = new ProviderConfig();
        providerConfig.setCurrency("EUR");
        providerConfig.setDestinationAccountNumber("DE89370400440532013000");
        providerConfig.setSenderName("Test Sender");
        providerConfig.setCountry("DE");
    }

    @Test
    void generateTransactions_ShouldGenerateCorrectNumberOfTransactions() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);
        int count = 5;

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", count);

        // Then
        assertThat(transactions).hasSize(count);
    }

    @Test
    void generateTransactions_ShouldGenerateUniqueTransactionIds() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);
        int count = 10;

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", count);

        // Then
        assertThat(transactions).hasSize(count);
        List<String> transactionIds = transactions.stream()
                .map(Transaction::getId)
                .toList();
        assertThat(transactionIds).doesNotHaveDuplicates();
    }

    @Test
    void generateTransactions_ShouldGenerateUniqueTrackingIds() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);
        int count = 10;

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", count);

        // Then
        List<String> trackingIds = transactions.stream()
                .map(Transaction::getTrackingId)
                .toList();
        assertThat(trackingIds).doesNotHaveDuplicates();
    }

    @Test
    void generateTransactions_ShouldSetCorrectProviderAndPaymentRail() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);
        String provider = "provider1";
        String paymentRail = "sepa";

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                provider, paymentRail, 3);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getProvider()).isEqualTo(provider);
            assertThat(transaction.getPaymentRail()).isEqualTo(paymentRail);
        });
    }

    @Test
    void generateTransactions_ShouldSetCurrencyFromConfig() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 2);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getCurrency()).isEqualTo("EUR");
        });
    }

    @Test
    void generateTransactions_ShouldSetDestinationAccountFromConfig() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 2);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getDestinationAccount().getAccountNumber())
                    .isEqualTo("DE89370400440532013000");
            assertThat(transaction.getDestinationAccount().getBic()).isNull();
            assertThat(transaction.getDestinationAccount().getIban()).isNull();
        });
    }

    @Test
    void generateTransactions_ShouldSetSenderDetailsFromConfig() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 2);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getSender().getName()).isEqualTo("Test Sender");
            assertThat(transaction.getSender().getCountry()).isEqualTo("DE");
            assertThat(transaction.getSender().getUltimateSenderName()).isEqualTo("ultimateDebtorName");
            assertThat(transaction.getSender().getFreeText()).isEqualTo("unstructuredOne, unstructuredTwo");
            assertThat(transaction.getSender().getAddress())
                    .isEqualTo("address line1, address line2, address line3, address line4");
        });
    }

    @Test
    void generateTransactions_ShouldSetOriginAccountWithFixedValues() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 2);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getOriginAccount().getAccountNumber())
                    .isEqualTo("FR26TCCL20786956994877");
            assertThat(transaction.getOriginAccount().getBic()).isEqualTo("CMBRFR2BARK");
            assertThat(transaction.getOriginAccount().getIban()).isEqualTo("FR26TCCL20786956994877");
        });
    }

    @Test
    void generateTransactions_ShouldSetVersionToTwo() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 2);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getVersion()).isEqualTo(2);
        });
    }

    @Test
    void generateTransactions_ShouldSetCreditDebitToCredit() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 2);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getCreditDebit()).isEqualTo("credit");
        });
    }

    @Test
    void generateTransactions_ShouldGenerateAmountBetween100And10000() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 50);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getAmount()).isBetween(100, 10000);
        });
    }

    @Test
    void generateTransactions_ShouldGenerateFutureDateBetween1And90Days() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.plusDays(1);
        LocalDate maxDate = today.plusDays(90);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 50);

        // Then
        transactions.forEach(transaction -> {
            LocalDate valueDate = LocalDate.parse(transaction.getValueDate(), DATE_FORMATTER);
            assertThat(valueDate).isBetween(minDate, maxDate);
        });
    }

    @Test
    void generateTransactions_ShouldGenerateReferenceWithCorrectFormat() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 10);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getReference()).startsWith("REF-");
            assertThat(transaction.getReference()).hasSize(12); // REF- + 8 characters
            assertThat(transaction.getReference()).matches("REF-[A-Z0-9]{8}");
        });
    }

    @Test
    void generateTransactions_ShouldGenerateUniqueReferences() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 20);

        // Then
        List<String> references = transactions.stream()
                .map(Transaction::getReference)
                .toList();
        assertThat(references).doesNotHaveDuplicates();
    }

    @Test
    void generateTransactions_ShouldSetTransactionContentToNull() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 2);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getTransactionContent()).isNull();
        });
    }

    @Test
    void generateTransactions_ShouldSetIdAndTrackingIdToSameValue() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 5);

        // Then
        transactions.forEach(transaction -> {
            assertThat(transaction.getId()).isEqualTo(transaction.getTrackingId());
        });
    }

    @Test
    void generateTransactions_ShouldThrowException_WhenConfigNotFound() {
        // Given
        when(configProperties.getConfig(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Provider not found"));

        // When & Then
        assertThatThrownBy(() -> transactionGeneratorService.generateTransactions(
                "invalid-provider", "sepa", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Provider not found");
    }

    @Test
    void generateTransactions_ShouldHandleZeroCount() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 0);

        // Then
        assertThat(transactions).isEmpty();
    }

    @Test
    void generateTransactions_ShouldHandleSingleTransaction() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", 1);

        // Then
        assertThat(transactions).hasSize(1);
        Transaction transaction = transactions.get(0);
        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getCurrency()).isEqualTo("EUR");
        assertThat(transaction.getProvider()).isEqualTo("provider1");
        assertThat(transaction.getPaymentRail()).isEqualTo("sepa");
    }

    @Test
    void generateTransactions_ShouldHandleLargeNumberOfTransactions() {
        // Given
        when(configProperties.getConfig(anyString(), anyString())).thenReturn(providerConfig);
        int largeCount = 1000;

        // When
        List<Transaction> transactions = transactionGeneratorService.generateTransactions(
                "provider1", "sepa", largeCount);

        // Then
        assertThat(transactions).hasSize(largeCount);
        List<String> ids = transactions.stream().map(Transaction::getId).toList();
        assertThat(ids).doesNotHaveDuplicates();
    }
}
