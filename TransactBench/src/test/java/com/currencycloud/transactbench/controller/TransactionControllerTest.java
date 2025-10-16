package com.currencycloud.transactbench.controller;

import com.currencycloud.transactbench.dto.TransactionGenerationRequest;
import com.currencycloud.transactbench.dto.TransactionGenerationResponse;
import com.currencycloud.transactbench.model.Account;
import com.currencycloud.transactbench.model.Sender;
import com.currencycloud.transactbench.model.Transaction;
import com.currencycloud.transactbench.service.TransactionGeneratorService;
import com.currencycloud.transactbench.service.TransactionPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionGeneratorService generatorService;

    @Mock
    private TransactionPublisherService publisherService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionGenerationRequest request;
    private List<Transaction> mockTransactions;

    @BeforeEach
    void setUp() {
        request = new TransactionGenerationRequest("provider1", "sepa", 2, null);
        mockTransactions = createMockTransactions();
    }

    @Test
    void generateTransactions_ShouldReturnCreatedWithTransactionIds_WhenSuccessful() {
        // Given
        when(generatorService.generateTransactions(anyString(), anyString(), anyInt(),isNull()))
                .thenReturn(mockTransactions);
        doNothing().when(publisherService).publishTransactions(any());

        // When
        ResponseEntity<TransactionGenerationResponse> response =
                transactionController.generateTransactions(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTransactionIds()).hasSize(2);
        assertThat(response.getBody().getTransactionIds())
                .containsExactly("txn-001", "txn-002");

        verify(generatorService).generateTransactions("provider1", "sepa", 2, null);
        verify(publisherService).publishTransactions(mockTransactions);
    }

    @Test
    void generateTransactions_ShouldReturnBadRequest_WhenInvalidConfiguration() {
        // Given
        when(generatorService.generateTransactions(anyString(), anyString(), anyInt(), isNull()))
                .thenThrow(new IllegalArgumentException("Provider not found"));

        // When
        ResponseEntity<TransactionGenerationResponse> response =
                transactionController.generateTransactions(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();

        verify(generatorService).generateTransactions("provider1", "sepa", 2, null);
        verify(publisherService, never()).publishTransactions(any());
    }

    @Test
    void generateTransactions_ShouldReturnInternalServerError_WhenGeneratorServiceThrowsException() {
        // Given
        when(generatorService.generateTransactions(anyString(), anyString(), anyInt(), isNull()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResponseEntity<TransactionGenerationResponse> response =
                transactionController.generateTransactions(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();

        verify(generatorService).generateTransactions("provider1", "sepa", 2, null);
        verify(publisherService, never()).publishTransactions(any());
    }

    @Test
    void generateTransactions_ShouldReturnInternalServerError_WhenPublisherServiceThrowsException() {
        // Given
        when(generatorService.generateTransactions(anyString(), anyString(), anyInt(), isNull()))
                .thenReturn(mockTransactions);
        doThrow(new RuntimeException("Failed to publish"))
                .when(publisherService).publishTransactions(any());

        // When
        ResponseEntity<TransactionGenerationResponse> response =
                transactionController.generateTransactions(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();

        verify(generatorService).generateTransactions("provider1", "sepa", 2, null);
        verify(publisherService).publishTransactions(mockTransactions);
    }

    @Test
    void generateTransactions_ShouldHandleEmptyTransactionList() {
        // Given
        when(generatorService.generateTransactions(anyString(), anyString(), anyInt(), isNull()))
                .thenReturn(Arrays.asList());
        doNothing().when(publisherService).publishTransactions(any());

        // When
        ResponseEntity<TransactionGenerationResponse> response =
                transactionController.generateTransactions(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTransactionIds()).isEmpty();

        verify(generatorService).generateTransactions("provider1", "sepa", 2, null);
        verify(publisherService).publishTransactions(any());
    }

  @Test
  void generateTransactions_ShouldHandleLargeNumberOfTransactions() {
    // Given
    TransactionGenerationRequest largeRequest =
        new TransactionGenerationRequest("provider1", "sepa", 1000, null);
    when(generatorService.generateTransactions(anyString(), anyString(), anyInt(), isNull()))
        .thenReturn(mockTransactions);
    doNothing().when(publisherService).publishTransactions(any());

    // When
    ResponseEntity<TransactionGenerationResponse> response =
        transactionController.generateTransactions(largeRequest);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();

    verify(generatorService).generateTransactions("provider1", "sepa", 1000, null);
    verify(publisherService).publishTransactions(mockTransactions);
  }

    private List<Transaction> createMockTransactions() {
        Account originAccount = Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .bic("CMBRFR2BARK")
                .iban("FR26TCCL20786956994877")
                .build();

        Account destAccount = Account.builder()
                .accountNumber("DE89370400440532013000")
                .build();

        Sender sender = Sender.builder()
                .freeText("test")
                .name("Test Sender")
                .ultimateSenderName("Ultimate Sender")
                .address("123 Test St")
                .country("FR")
                .build();

        Transaction txn1 = Transaction.builder()
                .id("txn-001")
                .version(2)
                .creditDebit("credit")
                .amount(1000)
                .currency("EUR")
                .valueDate("2025-10-20")
                .trackingId("txn-001")
                .reference("REF-001")
                .paymentRail("sepa")
                .provider("provider1")
                .originAccount(originAccount)
                .destinationAccount(destAccount)
                .sender(sender)
                .transactionContent(null)
                .build();

        Transaction txn2 = Transaction.builder()
                .id("txn-002")
                .version(2)
                .creditDebit("credit")
                .amount(2000)
                .currency("EUR")
                .valueDate("2025-10-21")
                .trackingId("txn-002")
                .reference("REF-002")
                .paymentRail("sepa")
                .provider("provider1")
                .originAccount(originAccount)
                .destinationAccount(destAccount)
                .sender(sender)
                .transactionContent(null)
                .build();

        return Arrays.asList(txn1, txn2);
    }
}
