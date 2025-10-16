package com.currencycloud.transactbench.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionGenerationRequestTest {

    private static Validator validator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void allArgsConstructor_ShouldCreateRequestWithAllFields() {
        // When
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // Then
        assertThat(request.getProvider()).isEqualTo("provider1");
        assertThat(request.getPaymentRail()).isEqualTo("sepa");
        assertThat(request.getNumberOfMessages()).isEqualTo(10);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyRequest() {
        // When
        TransactionGenerationRequest request = new TransactionGenerationRequest();

        // Then
        assertThat(request.getProvider()).isNull();
        assertThat(request.getPaymentRail()).isNull();
        assertThat(request.getNumberOfMessages()).isNull();
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest();

        // When
        request.setProvider("provider2");
        request.setPaymentRail("swift");
        request.setNumberOfMessages(20);

        // Then
        assertThat(request.getProvider()).isEqualTo("provider2");
        assertThat(request.getPaymentRail()).isEqualTo("swift");
        assertThat(request.getNumberOfMessages()).isEqualTo(20);
    }

    @Test
    void validation_ShouldPassWithValidData() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_ShouldFailWhenProviderIsNull() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                null, "sepa", 10
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Provider is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("provider");
    }

    @Test
    void validation_ShouldFailWhenProviderIsEmpty() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "", "sepa", 10
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Provider is required");
    }

    @Test
    void validation_ShouldFailWhenProviderIsBlank() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "   ", "sepa", 10
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Provider is required");
    }

    @Test
    void validation_ShouldFailWhenPaymentRailIsNull() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", null, 10
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Payment rail is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("paymentRail");
    }

    @Test
    void validation_ShouldFailWhenPaymentRailIsEmpty() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "", 10
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Payment rail is required");
    }

    @Test
    void validation_ShouldFailWhenPaymentRailIsBlank() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "   ", 10
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Payment rail is required");
    }

    @Test
    void validation_ShouldFailWhenNumberOfMessagesIsNull() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", null
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Number of messages is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("numberOfMessages");
    }

    @Test
    void validation_ShouldFailWhenNumberOfMessagesIsZero() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 0
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Number of messages must be at least 1");
    }

    @Test
    void validation_ShouldFailWhenNumberOfMessagesIsNegative() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", -5
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionGenerationRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Number of messages must be at least 1");
    }

    @Test
    void validation_ShouldPassWithMinimumValidNumberOfMessages() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 1
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_ShouldPassWithLargeNumberOfMessages() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 10000
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_ShouldFailWithMultipleViolations() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                null, null, null
        );

        // When
        Set<ConstraintViolation<TransactionGenerationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(3);
    }

    @Test
    void equals_ShouldReturnTrue_WhenRequestsAreEqual() {
        // Given
        TransactionGenerationRequest request1 = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );
        TransactionGenerationRequest request2 = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // When & Then
        assertThat(request1).isEqualTo(request2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenRequestsAreDifferent() {
        // Given
        TransactionGenerationRequest request1 = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );
        TransactionGenerationRequest request2 = new TransactionGenerationRequest(
                "provider2", "swift", 20
        );

        // When & Then
        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // When
        int hashCode1 = request.hashCode();
        int hashCode2 = request.hashCode();

        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void hashCode_ShouldBeEqual_WhenRequestsAreEqual() {
        // Given
        TransactionGenerationRequest request1 = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );
        TransactionGenerationRequest request2 = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // When & Then
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void toString_ShouldContainAllFields() {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("provider1");
        assertThat(result).contains("sepa");
        assertThat(result).contains("10");
    }

    @Test
    void jsonSerialization_ShouldSerializeCorrectly() throws Exception {
        // Given
        TransactionGenerationRequest request = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"provider\"");
        assertThat(json).contains("\"paymentRail\"");
        assertThat(json).contains("\"numberOfMessages\"");
        assertThat(json).contains("\"provider1\"");
        assertThat(json).contains("\"sepa\"");
        assertThat(json).contains("10");
    }

    @Test
    void jsonDeserialization_ShouldDeserializeCorrectly() throws Exception {
        // Given
        String json = """
                {
                    "provider": "provider1",
                    "paymentRail": "sepa",
                    "numberOfMessages": 10
                }
                """;

        // When
        TransactionGenerationRequest request = objectMapper.readValue(json, TransactionGenerationRequest.class);

        // Then
        assertThat(request.getProvider()).isEqualTo("provider1");
        assertThat(request.getPaymentRail()).isEqualTo("sepa");
        assertThat(request.getNumberOfMessages()).isEqualTo(10);
    }

    @Test
    void jsonRoundTrip_ShouldPreserveData() throws Exception {
        // Given
        TransactionGenerationRequest originalRequest = new TransactionGenerationRequest(
                "provider1", "sepa", 10
        );

        // When
        String json = objectMapper.writeValueAsString(originalRequest);
        TransactionGenerationRequest deserializedRequest = objectMapper.readValue(
                json, TransactionGenerationRequest.class);

        // Then
        assertThat(deserializedRequest).isEqualTo(originalRequest);
    }
}
