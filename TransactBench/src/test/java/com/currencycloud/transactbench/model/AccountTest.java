package com.currencycloud.transactbench.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void builder_ShouldCreateAccountWithAllFields() {
        // Given & When
        Account account = Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .bic("CMBRFR2BARK")
                .iban("FR26TCCL20786956994877")
                .build();

        // Then
        assertThat(account.getAccountNumber()).isEqualTo("FR26TCCL20786956994877");
        assertThat(account.getBic()).isEqualTo("CMBRFR2BARK");
        assertThat(account.getIban()).isEqualTo("FR26TCCL20786956994877");
    }

    @Test
    void builder_ShouldCreateAccountWithPartialFields() {
        // Given & When
        Account account = Account.builder()
                .accountNumber("DE89370400440532013000")
                .build();

        // Then
        assertThat(account.getAccountNumber()).isEqualTo("DE89370400440532013000");
        assertThat(account.getBic()).isNull();
        assertThat(account.getIban()).isNull();
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyAccount() {
        // When
        Account account = new Account();

        // Then
        assertThat(account.getAccountNumber()).isNull();
        assertThat(account.getBic()).isNull();
        assertThat(account.getIban()).isNull();
    }

    @Test
    void allArgsConstructor_ShouldCreateAccountWithAllFields() {
        // When
        Account account = new Account(
                "FR26TCCL20786956994877",
                "CMBRFR2BARK",
                "FR26TCCL20786956994877"
        );

        // Then
        assertThat(account.getAccountNumber()).isEqualTo("FR26TCCL20786956994877");
        assertThat(account.getBic()).isEqualTo("CMBRFR2BARK");
        assertThat(account.getIban()).isEqualTo("FR26TCCL20786956994877");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        Account account = new Account();

        // When
        account.setAccountNumber("GB33BUKB20201555555555");
        account.setBic("BUKBGB22");
        account.setIban("GB33BUKB20201555555555");

        // Then
        assertThat(account.getAccountNumber()).isEqualTo("GB33BUKB20201555555555");
        assertThat(account.getBic()).isEqualTo("BUKBGB22");
        assertThat(account.getIban()).isEqualTo("GB33BUKB20201555555555");
    }

    @Test
    void equals_ShouldReturnTrue_WhenAccountsAreEqual() {
        // Given
        Account account1 = Account.builder()
                .accountNumber("FR26")
                .bic("CMBR")
                .iban("FR26")
                .build();

        Account account2 = Account.builder()
                .accountNumber("FR26")
                .bic("CMBR")
                .iban("FR26")
                .build();

        // When & Then
        assertThat(account1).isEqualTo(account2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenAccountsAreDifferent() {
        // Given
        Account account1 = Account.builder()
                .accountNumber("FR26")
                .bic("CMBR")
                .iban("FR26")
                .build();

        Account account2 = Account.builder()
                .accountNumber("DE89")
                .bic("DEUT")
                .iban("DE89")
                .build();

        // When & Then
        assertThat(account1).isNotEqualTo(account2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        Account account = Account.builder()
                .accountNumber("FR26")
                .bic("CMBR")
                .iban("FR26")
                .build();

        // When
        int hashCode1 = account.hashCode();
        int hashCode2 = account.hashCode();

        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void hashCode_ShouldBeEqual_WhenAccountsAreEqual() {
        // Given
        Account account1 = Account.builder()
                .accountNumber("FR26")
                .bic("CMBR")
                .iban("FR26")
                .build();

        Account account2 = Account.builder()
                .accountNumber("FR26")
                .bic("CMBR")
                .iban("FR26")
                .build();

        // When & Then
        assertThat(account1.hashCode()).isEqualTo(account2.hashCode());
    }

    @Test
    void toString_ShouldContainAllFields() {
        // Given
        Account account = Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .bic("CMBRFR2BARK")
                .iban("FR26TCCL20786956994877")
                .build();

        // When
        String result = account.toString();

        // Then
        assertThat(result).contains("FR26TCCL20786956994877");
        assertThat(result).contains("CMBRFR2BARK");
    }

    @Test
    void jsonSerialization_ShouldUseCorrectPropertyNames() throws Exception {
        // Given
        Account account = Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .bic("CMBRFR2BARK")
                .iban("FR26TCCL20786956994877")
                .build();

        // When
        String json = objectMapper.writeValueAsString(account);

        // Then
        assertThat(json).contains("\"account_number\"");
        assertThat(json).contains("\"bic\"");
        assertThat(json).contains("\"iban\"");
    }

    @Test
    void jsonSerialization_ShouldExcludeNullFields() throws Exception {
        // Given
        Account account = Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .build();

        // When
        String json = objectMapper.writeValueAsString(account);

        // Then
        assertThat(json).contains("\"account_number\"");
        assertThat(json).doesNotContain("\"bic\"");
        assertThat(json).doesNotContain("\"iban\"");
    }

    @Test
    void jsonDeserialization_ShouldParseCorrectly() throws Exception {
        // Given
        String json = """
                {
                    "account_number": "FR26TCCL20786956994877",
                    "bic": "CMBRFR2BARK",
                    "iban": "FR26TCCL20786956994877"
                }
                """;

        // When
        Account account = objectMapper.readValue(json, Account.class);

        // Then
        assertThat(account.getAccountNumber()).isEqualTo("FR26TCCL20786956994877");
        assertThat(account.getBic()).isEqualTo("CMBRFR2BARK");
        assertThat(account.getIban()).isEqualTo("FR26TCCL20786956994877");
    }

    @Test
    void jsonDeserialization_ShouldHandleMissingFields() throws Exception {
        // Given
        String json = """
                {
                    "account_number": "FR26TCCL20786956994877"
                }
                """;

        // When
        Account account = objectMapper.readValue(json, Account.class);

        // Then
        assertThat(account.getAccountNumber()).isEqualTo("FR26TCCL20786956994877");
        assertThat(account.getBic()).isNull();
        assertThat(account.getIban()).isNull();
    }

    @Test
    void jsonRoundTrip_ShouldPreserveData() throws Exception {
        // Given
        Account originalAccount = Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .bic("CMBRFR2BARK")
                .iban("FR26TCCL20786956994877")
                .build();

        // When
        String json = objectMapper.writeValueAsString(originalAccount);
        Account deserializedAccount = objectMapper.readValue(json, Account.class);

        // Then
        assertThat(deserializedAccount).isEqualTo(originalAccount);
    }

    @Test
    void builder_ShouldHandleNullValues() {
        // Given & When
        Account account = Account.builder()
                .accountNumber(null)
                .bic(null)
                .iban(null)
                .build();

        // Then
        assertThat(account.getAccountNumber()).isNull();
        assertThat(account.getBic()).isNull();
        assertThat(account.getIban()).isNull();
    }

    @Test
    void setters_ShouldHandleNullValues() {
        // Given
        Account account = Account.builder()
                .accountNumber("FR26")
                .bic("CMBR")
                .iban("FR26")
                .build();

        // When
        account.setAccountNumber(null);
        account.setBic(null);
        account.setIban(null);

        // Then
        assertThat(account.getAccountNumber()).isNull();
        assertThat(account.getBic()).isNull();
        assertThat(account.getIban()).isNull();
    }
}
