package com.currencycloud.transactbench.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SenderTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void builder_ShouldCreateSenderWithAllFields() {
        // Given & When
        Sender sender = Sender.builder()
                .freeText("unstructuredOne, unstructuredTwo")
                .name("Test Sender")
                .ultimateSenderName("Ultimate Sender")
                .address("address line1, address line2, address line3, address line4")
                .country("FR")
                .build();

        // Then
        assertThat(sender.getFreeText()).isEqualTo("unstructuredOne, unstructuredTwo");
        assertThat(sender.getName()).isEqualTo("Test Sender");
        assertThat(sender.getUltimateSenderName()).isEqualTo("Ultimate Sender");
        assertThat(sender.getAddress()).isEqualTo("address line1, address line2, address line3, address line4");
        assertThat(sender.getCountry()).isEqualTo("FR");
    }

    @Test
    void builder_ShouldCreateSenderWithPartialFields() {
        // Given & When
        Sender sender = Sender.builder()
                .name("Test Sender")
                .country("DE")
                .build();

        // Then
        assertThat(sender.getName()).isEqualTo("Test Sender");
        assertThat(sender.getCountry()).isEqualTo("DE");
        assertThat(sender.getFreeText()).isNull();
        assertThat(sender.getUltimateSenderName()).isNull();
        assertThat(sender.getAddress()).isNull();
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptySender() {
        // When
        Sender sender = new Sender();

        // Then
        assertThat(sender.getFreeText()).isNull();
        assertThat(sender.getName()).isNull();
        assertThat(sender.getUltimateSenderName()).isNull();
        assertThat(sender.getAddress()).isNull();
        assertThat(sender.getCountry()).isNull();
    }

    @Test
    void allArgsConstructor_ShouldCreateSenderWithAllFields() {
        // When
        Sender sender = new Sender(
                "unstructuredOne, unstructuredTwo",
                "Test Sender",
                "Ultimate Sender",
                "address line1, address line2",
                "FR"
        );

        // Then
        assertThat(sender.getFreeText()).isEqualTo("unstructuredOne, unstructuredTwo");
        assertThat(sender.getName()).isEqualTo("Test Sender");
        assertThat(sender.getUltimateSenderName()).isEqualTo("Ultimate Sender");
        assertThat(sender.getAddress()).isEqualTo("address line1, address line2");
        assertThat(sender.getCountry()).isEqualTo("FR");
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        Sender sender = new Sender();

        // When
        sender.setFreeText("new free text");
        sender.setName("New Sender");
        sender.setUltimateSenderName("New Ultimate Sender");
        sender.setAddress("new address");
        sender.setCountry("GB");

        // Then
        assertThat(sender.getFreeText()).isEqualTo("new free text");
        assertThat(sender.getName()).isEqualTo("New Sender");
        assertThat(sender.getUltimateSenderName()).isEqualTo("New Ultimate Sender");
        assertThat(sender.getAddress()).isEqualTo("new address");
        assertThat(sender.getCountry()).isEqualTo("GB");
    }

    @Test
    void equals_ShouldReturnTrue_WhenSendersAreEqual() {
        // Given
        Sender sender1 = Sender.builder()
                .freeText("text")
                .name("Test Sender")
                .ultimateSenderName("Ultimate")
                .address("address")
                .country("FR")
                .build();

        Sender sender2 = Sender.builder()
                .freeText("text")
                .name("Test Sender")
                .ultimateSenderName("Ultimate")
                .address("address")
                .country("FR")
                .build();

        // When & Then
        assertThat(sender1).isEqualTo(sender2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenSendersAreDifferent() {
        // Given
        Sender sender1 = Sender.builder()
                .name("Test Sender")
                .country("FR")
                .build();

        Sender sender2 = Sender.builder()
                .name("Different Sender")
                .country("DE")
                .build();

        // When & Then
        assertThat(sender1).isNotEqualTo(sender2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        Sender sender = Sender.builder()
                .name("Test Sender")
                .country("FR")
                .build();

        // When
        int hashCode1 = sender.hashCode();
        int hashCode2 = sender.hashCode();

        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void hashCode_ShouldBeEqual_WhenSendersAreEqual() {
        // Given
        Sender sender1 = Sender.builder()
                .name("Test Sender")
                .country("FR")
                .build();

        Sender sender2 = Sender.builder()
                .name("Test Sender")
                .country("FR")
                .build();

        // When & Then
        assertThat(sender1.hashCode()).isEqualTo(sender2.hashCode());
    }

    @Test
    void toString_ShouldContainAllFields() {
        // Given
        Sender sender = Sender.builder()
                .freeText("unstructuredOne, unstructuredTwo")
                .name("Test Sender")
                .ultimateSenderName("Ultimate Sender")
                .address("address line1")
                .country("FR")
                .build();

        // When
        String result = sender.toString();

        // Then
        assertThat(result).contains("Test Sender");
        assertThat(result).contains("FR");
        assertThat(result).contains("Ultimate Sender");
    }

    @Test
    void jsonSerialization_ShouldUseCorrectPropertyNames() throws Exception {
        // Given
        Sender sender = Sender.builder()
                .freeText("unstructuredOne, unstructuredTwo")
                .name("Test Sender")
                .ultimateSenderName("Ultimate Sender")
                .address("address line1")
                .country("FR")
                .build();

        // When
        String json = objectMapper.writeValueAsString(sender);

        // Then
        assertThat(json).contains("\"free_text\"");
        assertThat(json).contains("\"name\"");
        assertThat(json).contains("\"ultimate_sender_name\"");
        assertThat(json).contains("\"address\"");
        assertThat(json).contains("\"country\"");
    }

    @Test
    void jsonDeserialization_ShouldParseCorrectly() throws Exception {
        // Given
        String json = """
                {
                    "free_text": "unstructuredOne, unstructuredTwo",
                    "name": "Test Sender",
                    "ultimate_sender_name": "Ultimate Sender",
                    "address": "address line1, address line2",
                    "country": "FR"
                }
                """;

        // When
        Sender sender = objectMapper.readValue(json, Sender.class);

        // Then
        assertThat(sender.getFreeText()).isEqualTo("unstructuredOne, unstructuredTwo");
        assertThat(sender.getName()).isEqualTo("Test Sender");
        assertThat(sender.getUltimateSenderName()).isEqualTo("Ultimate Sender");
        assertThat(sender.getAddress()).isEqualTo("address line1, address line2");
        assertThat(sender.getCountry()).isEqualTo("FR");
    }

    @Test
    void jsonDeserialization_ShouldHandleMissingFields() throws Exception {
        // Given
        String json = """
                {
                    "name": "Test Sender",
                    "country": "FR"
                }
                """;

        // When
        Sender sender = objectMapper.readValue(json, Sender.class);

        // Then
        assertThat(sender.getName()).isEqualTo("Test Sender");
        assertThat(sender.getCountry()).isEqualTo("FR");
        assertThat(sender.getFreeText()).isNull();
        assertThat(sender.getUltimateSenderName()).isNull();
        assertThat(sender.getAddress()).isNull();
    }

    @Test
    void jsonRoundTrip_ShouldPreserveData() throws Exception {
        // Given
        Sender originalSender = Sender.builder()
                .freeText("unstructuredOne, unstructuredTwo")
                .name("Test Sender")
                .ultimateSenderName("Ultimate Sender")
                .address("address line1, address line2, address line3, address line4")
                .country("FR")
                .build();

        // When
        String json = objectMapper.writeValueAsString(originalSender);
        Sender deserializedSender = objectMapper.readValue(json, Sender.class);

        // Then
        assertThat(deserializedSender).isEqualTo(originalSender);
    }

    @Test
    void builder_ShouldHandleNullValues() {
        // Given & When
        Sender sender = Sender.builder()
                .freeText(null)
                .name(null)
                .ultimateSenderName(null)
                .address(null)
                .country(null)
                .build();

        // Then
        assertThat(sender.getFreeText()).isNull();
        assertThat(sender.getName()).isNull();
        assertThat(sender.getUltimateSenderName()).isNull();
        assertThat(sender.getAddress()).isNull();
        assertThat(sender.getCountry()).isNull();
    }

    @Test
    void setters_ShouldHandleNullValues() {
        // Given
        Sender sender = Sender.builder()
                .freeText("text")
                .name("Test Sender")
                .ultimateSenderName("Ultimate")
                .address("address")
                .country("FR")
                .build();

        // When
        sender.setFreeText(null);
        sender.setName(null);
        sender.setUltimateSenderName(null);
        sender.setAddress(null);
        sender.setCountry(null);

        // Then
        assertThat(sender.getFreeText()).isNull();
        assertThat(sender.getName()).isNull();
        assertThat(sender.getUltimateSenderName()).isNull();
        assertThat(sender.getAddress()).isNull();
        assertThat(sender.getCountry()).isNull();
    }

    @Test
    void builder_ShouldHandleEmptyStrings() {
        // Given & When
        Sender sender = Sender.builder()
                .freeText("")
                .name("")
                .ultimateSenderName("")
                .address("")
                .country("")
                .build();

        // Then
        assertThat(sender.getFreeText()).isEmpty();
        assertThat(sender.getName()).isEmpty();
        assertThat(sender.getUltimateSenderName()).isEmpty();
        assertThat(sender.getAddress()).isEmpty();
        assertThat(sender.getCountry()).isEmpty();
    }

    @Test
    void builder_ShouldHandleSpecialCharactersInFields() {
        // Given & When
        Sender sender = Sender.builder()
                .freeText("Special chars: éàü, 日本語")
                .name("Müller & Co.")
                .ultimateSenderName("O'Brien")
                .address("123 Main St., Apt. #4B")
                .country("FR")
                .build();

        // Then
        assertThat(sender.getFreeText()).isEqualTo("Special chars: éàü, 日本語");
        assertThat(sender.getName()).isEqualTo("Müller & Co.");
        assertThat(sender.getUltimateSenderName()).isEqualTo("O'Brien");
        assertThat(sender.getAddress()).isEqualTo("123 Main St., Apt. #4B");
    }

    @Test
    void builder_ShouldHandleLongAddressField() {
        // Given
        String longAddress = "address line1, address line2, address line3, address line4, " +
                "address line5, address line6, address line7, address line8";

        // When
        Sender sender = Sender.builder()
                .address(longAddress)
                .build();

        // Then
        assertThat(sender.getAddress()).isEqualTo(longAddress);
    }
}
