package com.currencycloud.transactbench.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionGenerationResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void allArgsConstructor_ShouldCreateResponseWithTransactionIds() {
        // Given
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002", "txn-003");

        // When
        TransactionGenerationResponse response = new TransactionGenerationResponse(transactionIds);

        // Then
        assertThat(response.getTransactionIds()).isEqualTo(transactionIds);
        assertThat(response.getTransactionIds()).hasSize(3);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyResponse() {
        // When
        TransactionGenerationResponse response = new TransactionGenerationResponse();

        // Then
        assertThat(response.getTransactionIds()).isNull();
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        TransactionGenerationResponse response = new TransactionGenerationResponse();
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002");

        // When
        response.setTransactionIds(transactionIds);

        // Then
        assertThat(response.getTransactionIds()).isEqualTo(transactionIds);
    }

    @Test
    void constructor_ShouldHandleEmptyList() {
        // Given
        List<String> emptyList = Collections.emptyList();

        // When
        TransactionGenerationResponse response = new TransactionGenerationResponse(emptyList);

        // Then
        assertThat(response.getTransactionIds()).isEmpty();
    }

    @Test
    void constructor_ShouldHandleSingleTransactionId() {
        // Given
        List<String> singleId = Collections.singletonList("txn-001");

        // When
        TransactionGenerationResponse response = new TransactionGenerationResponse(singleId);

        // Then
        assertThat(response.getTransactionIds()).hasSize(1);
        assertThat(response.getTransactionIds().get(0)).isEqualTo("txn-001");
    }

    @Test
    void constructor_ShouldHandleLargeListOfTransactionIds() {
        // Given
        List<String> largeList = Arrays.asList(
                "txn-001", "txn-002", "txn-003", "txn-004", "txn-005",
                "txn-006", "txn-007", "txn-008", "txn-009", "txn-010"
        );

        // When
        TransactionGenerationResponse response = new TransactionGenerationResponse(largeList);

        // Then
        assertThat(response.getTransactionIds()).hasSize(10);
        assertThat(response.getTransactionIds()).containsExactlyElementsOf(largeList);
    }

    @Test
    void equals_ShouldReturnTrue_WhenResponsesAreEqual() {
        // Given
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002");
        TransactionGenerationResponse response1 = new TransactionGenerationResponse(transactionIds);
        TransactionGenerationResponse response2 = new TransactionGenerationResponse(transactionIds);

        // When & Then
        assertThat(response1).isEqualTo(response2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenResponsesAreDifferent() {
        // Given
        TransactionGenerationResponse response1 = new TransactionGenerationResponse(
                Arrays.asList("txn-001", "txn-002")
        );
        TransactionGenerationResponse response2 = new TransactionGenerationResponse(
                Arrays.asList("txn-003", "txn-004")
        );

        // When & Then
        assertThat(response1).isNotEqualTo(response2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenListSizesAreDifferent() {
        // Given
        TransactionGenerationResponse response1 = new TransactionGenerationResponse(
                Arrays.asList("txn-001", "txn-002", "txn-003")
        );
        TransactionGenerationResponse response2 = new TransactionGenerationResponse(
                Arrays.asList("txn-001", "txn-002")
        );

        // When & Then
        assertThat(response1).isNotEqualTo(response2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002");
        TransactionGenerationResponse response = new TransactionGenerationResponse(transactionIds);

        // When
        int hashCode1 = response.hashCode();
        int hashCode2 = response.hashCode();

        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void hashCode_ShouldBeEqual_WhenResponsesAreEqual() {
        // Given
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002");
        TransactionGenerationResponse response1 = new TransactionGenerationResponse(transactionIds);
        TransactionGenerationResponse response2 = new TransactionGenerationResponse(transactionIds);

        // When & Then
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void toString_ShouldContainTransactionIds() {
        // Given
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002");
        TransactionGenerationResponse response = new TransactionGenerationResponse(transactionIds);

        // When
        String result = response.toString();

        // Then
        assertThat(result).contains("txn-001");
        assertThat(result).contains("txn-002");
    }

    @Test
    void jsonSerialization_ShouldSerializeCorrectly() throws Exception {
        // Given
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002", "txn-003");
        TransactionGenerationResponse response = new TransactionGenerationResponse(transactionIds);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"transactionIds\"");
        assertThat(json).contains("\"txn-001\"");
        assertThat(json).contains("\"txn-002\"");
        assertThat(json).contains("\"txn-003\"");
    }

    @Test
    void jsonSerialization_ShouldSerializeEmptyList() throws Exception {
        // Given
        TransactionGenerationResponse response = new TransactionGenerationResponse(Collections.emptyList());

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"transactionIds\"");
        assertThat(json).contains("[]");
    }

    @Test
    void jsonDeserialization_ShouldDeserializeCorrectly() throws Exception {
        // Given
        String json = """
                {
                    "transactionIds": ["txn-001", "txn-002", "txn-003"]
                }
                """;

        // When
        TransactionGenerationResponse response = objectMapper.readValue(json, TransactionGenerationResponse.class);

        // Then
        assertThat(response.getTransactionIds()).hasSize(3);
        assertThat(response.getTransactionIds()).containsExactly("txn-001", "txn-002", "txn-003");
    }

    @Test
    void jsonDeserialization_ShouldDeserializeEmptyList() throws Exception {
        // Given
        String json = """
                {
                    "transactionIds": []
                }
                """;

        // When
        TransactionGenerationResponse response = objectMapper.readValue(json, TransactionGenerationResponse.class);

        // Then
        assertThat(response.getTransactionIds()).isEmpty();
    }

    @Test
    void jsonDeserialization_ShouldHandleNullTransactionIds() throws Exception {
        // Given
        String json = """
                {
                    "transactionIds": null
                }
                """;

        // When
        TransactionGenerationResponse response = objectMapper.readValue(json, TransactionGenerationResponse.class);

        // Then
        assertThat(response.getTransactionIds()).isNull();
    }

    @Test
    void jsonRoundTrip_ShouldPreserveData() throws Exception {
        // Given
        List<String> transactionIds = Arrays.asList("txn-001", "txn-002", "txn-003");
        TransactionGenerationResponse originalResponse = new TransactionGenerationResponse(transactionIds);

        // When
        String json = objectMapper.writeValueAsString(originalResponse);
        TransactionGenerationResponse deserializedResponse = objectMapper.readValue(
                json, TransactionGenerationResponse.class);

        // Then
        assertThat(deserializedResponse).isEqualTo(originalResponse);
        assertThat(deserializedResponse.getTransactionIds()).containsExactlyElementsOf(transactionIds);
    }

    @Test
    void constructor_ShouldHandleNullTransactionIds() {
        // When
        TransactionGenerationResponse response = new TransactionGenerationResponse(null);

        // Then
        assertThat(response.getTransactionIds()).isNull();
    }

    @Test
    void setter_ShouldAllowSettingNullTransactionIds() {
        // Given
        TransactionGenerationResponse response = new TransactionGenerationResponse(
                Arrays.asList("txn-001", "txn-002")
        );

        // When
        response.setTransactionIds(null);

        // Then
        assertThat(response.getTransactionIds()).isNull();
    }

    @Test
    void setter_ShouldAllowReplacingTransactionIds() {
        // Given
        TransactionGenerationResponse response = new TransactionGenerationResponse(
                Arrays.asList("txn-001", "txn-002")
        );
        List<String> newTransactionIds = Arrays.asList("txn-003", "txn-004", "txn-005");

        // When
        response.setTransactionIds(newTransactionIds);

        // Then
        assertThat(response.getTransactionIds()).isEqualTo(newTransactionIds);
        assertThat(response.getTransactionIds()).hasSize(3);
    }

    @Test
    void jsonSerialization_ShouldHandleUUIDFormattedIds() throws Exception {
        // Given
        List<String> uuidIds = Arrays.asList(
                "550e8400-e29b-41d4-a716-446655440000",
                "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
                "6ba7b811-9dad-11d1-80b4-00c04fd430c8"
        );
        TransactionGenerationResponse response = new TransactionGenerationResponse(uuidIds);

        // When
        String json = objectMapper.writeValueAsString(response);
        TransactionGenerationResponse deserializedResponse = objectMapper.readValue(
                json, TransactionGenerationResponse.class);

        // Then
        assertThat(deserializedResponse.getTransactionIds()).containsExactlyElementsOf(uuidIds);
    }

    @Test
    void constructor_ShouldPreserveOrderOfTransactionIds() {
        // Given
        List<String> orderedIds = Arrays.asList("txn-003", "txn-001", "txn-005", "txn-002", "txn-004");

        // When
        TransactionGenerationResponse response = new TransactionGenerationResponse(orderedIds);

        // Then
        assertThat(response.getTransactionIds()).containsExactly("txn-003", "txn-001", "txn-005", "txn-002", "txn-004");
    }
}
