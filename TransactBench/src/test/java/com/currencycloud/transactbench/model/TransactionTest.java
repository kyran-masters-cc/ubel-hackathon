package com.currencycloud.transactbench.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void builder_ShouldCreateTransactionWithAllFields() {
        // Given & When
        Transaction transaction = Transaction.builder()
                .id("txn-001")
                .version(2)
                .creditDebit("credit")
                .amount(1000)
                .currency("EUR")
                .valueDate("2025-10-20")
                .trackingId("track-001")
                .reference("REF-001")
                .paymentRail("sepa")
                .provider("provider1")
                .originAccount(createAccount())
                .destinationAccount(createAccount())
                .sender(createSender())
                .transactionContent(null)
                .build();

        // Then
        assertThat(transaction.getId()).isEqualTo("txn-001");
        assertThat(transaction.getVersion()).isEqualTo(2);
        assertThat(transaction.getCreditDebit()).isEqualTo("credit");
        assertThat(transaction.getAmount()).isEqualTo(1000);
        assertThat(transaction.getCurrency()).isEqualTo("EUR");
        assertThat(transaction.getValueDate()).isEqualTo("2025-10-20");
        assertThat(transaction.getTrackingId()).isEqualTo("track-001");
        assertThat(transaction.getReference()).isEqualTo("REF-001");
        assertThat(transaction.getPaymentRail()).isEqualTo("sepa");
        assertThat(transaction.getProvider()).isEqualTo("provider1");
        assertThat(transaction.getOriginAccount()).isNotNull();
        assertThat(transaction.getDestinationAccount()).isNotNull();
        assertThat(transaction.getSender()).isNotNull();
        assertThat(transaction.getTransactionContent()).isNull();
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyTransaction() {
        // When
        Transaction transaction = new Transaction();

        // Then
        assertThat(transaction.getId()).isNull();
        assertThat(transaction.getVersion()).isNull();
        assertThat(transaction.getCreditDebit()).isNull();
        assertThat(transaction.getAmount()).isNull();
        assertThat(transaction.getCurrency()).isNull();
        assertThat(transaction.getValueDate()).isNull();
        assertThat(transaction.getTrackingId()).isNull();
        assertThat(transaction.getReference()).isNull();
        assertThat(transaction.getPaymentRail()).isNull();
        assertThat(transaction.getProvider()).isNull();
        assertThat(transaction.getOriginAccount()).isNull();
        assertThat(transaction.getDestinationAccount()).isNull();
        assertThat(transaction.getSender()).isNull();
        assertThat(transaction.getTransactionContent()).isNull();
    }

    @Test
    void allArgsConstructor_ShouldCreateTransactionWithAllFields() {
        // Given
        Account originAccount = createAccount();
        Account destinationAccount = createAccount();
        Sender sender = createSender();

        // When
        Transaction transaction = new Transaction(
                "txn-001", 2, "credit", 1000, "EUR", "2025-10-20",
                "track-001", "REF-001", "sepa", "provider1",
                originAccount, destinationAccount, sender, null
        );

        // Then
        assertThat(transaction.getId()).isEqualTo("txn-001");
        assertThat(transaction.getVersion()).isEqualTo(2);
        assertThat(transaction.getCreditDebit()).isEqualTo("credit");
        assertThat(transaction.getAmount()).isEqualTo(1000);
        assertThat(transaction.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        Transaction transaction = new Transaction();

        // When
        transaction.setId("txn-002");
        transaction.setVersion(3);
        transaction.setCreditDebit("debit");
        transaction.setAmount(2000);
        transaction.setCurrency("USD");
        transaction.setValueDate("2025-10-21");
        transaction.setTrackingId("track-002");
        transaction.setReference("REF-002");
        transaction.setPaymentRail("swift");
        transaction.setProvider("provider2");
        transaction.setOriginAccount(createAccount());
        transaction.setDestinationAccount(createAccount());
        transaction.setSender(createSender());
        transaction.setTransactionContent("content");

        // Then
        assertThat(transaction.getId()).isEqualTo("txn-002");
        assertThat(transaction.getVersion()).isEqualTo(3);
        assertThat(transaction.getCreditDebit()).isEqualTo("debit");
        assertThat(transaction.getAmount()).isEqualTo(2000);
        assertThat(transaction.getCurrency()).isEqualTo("USD");
        assertThat(transaction.getValueDate()).isEqualTo("2025-10-21");
        assertThat(transaction.getTrackingId()).isEqualTo("track-002");
        assertThat(transaction.getReference()).isEqualTo("REF-002");
        assertThat(transaction.getPaymentRail()).isEqualTo("swift");
        assertThat(transaction.getProvider()).isEqualTo("provider2");
        assertThat(transaction.getTransactionContent()).isEqualTo("content");
    }

    @Test
    void equals_ShouldReturnTrue_WhenTransactionsAreEqual() {
        // Given
        Transaction transaction1 = createFullTransaction();
        Transaction transaction2 = createFullTransaction();

        // When & Then
        assertThat(transaction1).isEqualTo(transaction2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenTransactionsAreDifferent() {
        // Given
        Transaction transaction1 = createFullTransaction();
        Transaction transaction2 = createFullTransaction();
        transaction2.setId("different-id");

        // When & Then
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        Transaction transaction = createFullTransaction();

        // When
        int hashCode1 = transaction.hashCode();
        int hashCode2 = transaction.hashCode();

        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void hashCode_ShouldBeEqual_WhenTransactionsAreEqual() {
        // Given
        Transaction transaction1 = createFullTransaction();
        Transaction transaction2 = createFullTransaction();

        // When & Then
        assertThat(transaction1.hashCode()).isEqualTo(transaction2.hashCode());
    }

    @Test
    void toString_ShouldContainAllFields() {
        // Given
        Transaction transaction = createFullTransaction();

        // When
        String result = transaction.toString();

        // Then
        assertThat(result).contains("txn-001");
        assertThat(result).contains("credit");
        assertThat(result).contains("EUR");
        assertThat(result).contains("sepa");
        assertThat(result).contains("provider1");
    }

    @Test
    void jsonSerialization_ShouldUseCorrectPropertyNames() throws Exception {
        // Given
        Transaction transaction = Transaction.builder()
                .id("txn-001")
                .version(2)
                .creditDebit("credit")
                .amount(1000)
                .currency("EUR")
                .valueDate("2025-10-20")
                .trackingId("track-001")
                .reference("REF-001")
                .paymentRail("sepa")
                .provider("provider1")
                .originAccount(createAccount())
                .destinationAccount(createAccount())
                .sender(createSender())
                .transactionContent(null)
                .build();

        // When
        String json = objectMapper.writeValueAsString(transaction);

        // Then
        assertThat(json).contains("\"credit_debit\"");
        assertThat(json).contains("\"value_date\"");
        assertThat(json).contains("\"tracking_id\"");
        assertThat(json).contains("\"payment_rail\"");
        assertThat(json).contains("\"origin_account\"");
        assertThat(json).contains("\"destination_account\"");
        assertThat(json).contains("\"transaction_content\"");
    }

    @Test
    void jsonDeserialization_ShouldParseCorrectly() throws Exception {
        // Given
        String json = """
                {
                    "id": "txn-001",
                    "version": 2,
                    "credit_debit": "credit",
                    "amount": 1000,
                    "currency": "EUR",
                    "value_date": "2025-10-20",
                    "tracking_id": "track-001",
                    "reference": "REF-001",
                    "payment_rail": "sepa",
                    "provider": "provider1",
                    "origin_account": {"account_number": "FR26"},
                    "destination_account": {"account_number": "DE89"},
                    "sender": {"name": "Test"},
                    "transaction_content": null
                }
                """;

        // When
        Transaction transaction = objectMapper.readValue(json, Transaction.class);

        // Then
        assertThat(transaction.getId()).isEqualTo("txn-001");
        assertThat(transaction.getVersion()).isEqualTo(2);
        assertThat(transaction.getCreditDebit()).isEqualTo("credit");
        assertThat(transaction.getAmount()).isEqualTo(1000);
        assertThat(transaction.getCurrency()).isEqualTo("EUR");
        assertThat(transaction.getValueDate()).isEqualTo("2025-10-20");
        assertThat(transaction.getTrackingId()).isEqualTo("track-001");
        assertThat(transaction.getReference()).isEqualTo("REF-001");
        assertThat(transaction.getPaymentRail()).isEqualTo("sepa");
        assertThat(transaction.getProvider()).isEqualTo("provider1");
        assertThat(transaction.getTransactionContent()).isNull();
    }

    private Transaction createFullTransaction() {
        return Transaction.builder()
                .id("txn-001")
                .version(2)
                .creditDebit("credit")
                .amount(1000)
                .currency("EUR")
                .valueDate("2025-10-20")
                .trackingId("track-001")
                .reference("REF-001")
                .paymentRail("sepa")
                .provider("provider1")
                .originAccount(createAccount())
                .destinationAccount(createAccount())
                .sender(createSender())
                .transactionContent(null)
                .build();
    }

    private Account createAccount() {
        return Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .bic("CMBRFR2BARK")
                .iban("FR26TCCL20786956994877")
                .build();
    }

    private Sender createSender() {
        return Sender.builder()
                .freeText("test")
                .name("Test Sender")
                .ultimateSenderName("Ultimate Sender")
                .address("123 Test St")
                .country("FR")
                .build();
    }
}
