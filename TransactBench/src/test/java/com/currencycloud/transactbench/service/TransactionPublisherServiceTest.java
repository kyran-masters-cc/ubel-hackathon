package com.currencycloud.transactbench.service;

import com.currencycloud.transactbench.model.Account;
import com.currencycloud.transactbench.model.Sender;
import com.currencycloud.transactbench.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionPublisherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TransactionPublisherService transactionPublisherService;

    private List<Transaction> transactions;
    private static final String EXCHANGE_NAME = "transaction-exchange";
    private static final String ROUTING_KEY = "new_transaction_message";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionPublisherService, "exchangeName", EXCHANGE_NAME);
        ReflectionTestUtils.setField(transactionPublisherService, "routingKey", ROUTING_KEY);
        transactions = createMockTransactions();
    }

    @Test
    void publishTransactions_ShouldPublishAllTransactions_WhenSuccessful() {
        // Given
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When
        transactionPublisherService.publishTransactions(transactions);

        // Then
        verify(rabbitTemplate, times(2)).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                any(Transaction.class)
        );
    }

    @Test
    void publishTransactions_ShouldPublishEachTransactionIndividually() {
        // Given
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When
        transactionPublisherService.publishTransactions(transactions);

        // Then
        verify(rabbitTemplate).convertAndSend(EXCHANGE_NAME, ROUTING_KEY, transactions.get(0));
        verify(rabbitTemplate).convertAndSend(EXCHANGE_NAME, ROUTING_KEY, transactions.get(1));
    }

    @Test
    void publishTransactions_ShouldThrowException_WhenRabbitTemplateFailsOnFirstTransaction() {
        // Given
        doThrow(new RuntimeException("RabbitMQ connection error"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When & Then
        assertThatThrownBy(() -> transactionPublisherService.publishTransactions(transactions))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to publish transaction");

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                any(Transaction.class)
        );
    }

    @Test
    void publishTransactions_ShouldThrowException_WhenRabbitTemplateFailsOnSecondTransaction() {
        // Given
        doNothing()
                .doThrow(new RuntimeException("RabbitMQ connection error"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When & Then
        assertThatThrownBy(() -> transactionPublisherService.publishTransactions(transactions))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to publish transaction");

        verify(rabbitTemplate, times(2)).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                any(Transaction.class)
        );
    }

    @Test
    void publishTransactions_ShouldHandleEmptyList() {
        // Given
        List<Transaction> emptyList = Collections.emptyList();

        // When
        transactionPublisherService.publishTransactions(emptyList);

        // Then
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void publishTransactions_ShouldHandleSingleTransaction() {
        // Given
        List<Transaction> singleTransaction = Collections.singletonList(transactions.get(0));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When
        transactionPublisherService.publishTransactions(singleTransaction);

        // Then
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                eq(transactions.get(0))
        );
    }

    @Test
    void publishTransactions_ShouldHandleLargeNumberOfTransactions() {
        // Given
        List<Transaction> largeList = createLargeTransactionList(100);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When
        transactionPublisherService.publishTransactions(largeList);

        // Then
        verify(rabbitTemplate, times(100)).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                any(Transaction.class)
        );
    }

    @Test
    void publishTransactions_ShouldUseCorrectExchangeAndRoutingKey() {
        // Given
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When
        transactionPublisherService.publishTransactions(transactions);

        // Then
        verify(rabbitTemplate, atLeastOnce()).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                any(Transaction.class)
        );
    }

    @Test
    void publishTransactions_ShouldUseCustomExchangeName_WhenConfigured() {
        // Given
        String customExchange = "custom-exchange";
        ReflectionTestUtils.setField(transactionPublisherService, "exchangeName", customExchange);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When
        transactionPublisherService.publishTransactions(transactions);

        // Then
        verify(rabbitTemplate, times(2)).convertAndSend(
                eq(customExchange),
                eq(ROUTING_KEY),
                any(Transaction.class)
        );
    }

    @Test
    void publishTransactions_ShouldUseCustomRoutingKey_WhenConfigured() {
        // Given
        String customRoutingKey = "custom_routing_key";
        ReflectionTestUtils.setField(transactionPublisherService, "routingKey", customRoutingKey);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When
        transactionPublisherService.publishTransactions(transactions);

        // Then
        verify(rabbitTemplate, times(2)).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(customRoutingKey),
                any(Transaction.class)
        );
    }

    @Test
    void publishTransactions_ShouldIncludeTransactionIdInException_WhenPublishFails() {
        // Given
        Transaction failingTransaction = transactions.get(0);
        doThrow(new RuntimeException("Network error"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When & Then
        assertThatThrownBy(() -> transactionPublisherService.publishTransactions(
                Collections.singletonList(failingTransaction)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to publish transaction")
                .hasMessageContaining(failingTransaction.getId());
    }

    @Test
    void publishTransactions_ShouldPreserveCauseException_WhenPublishFails() {
        // Given
        RuntimeException originalException = new RuntimeException("Original error");
        doThrow(originalException)
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When & Then
        assertThatThrownBy(() -> transactionPublisherService.publishTransactions(transactions))
                .isInstanceOf(RuntimeException.class)
                .hasCause(originalException);
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

    private List<Transaction> createLargeTransactionList(int count) {
        Transaction template = createMockTransactions().get(0);
        return Collections.nCopies(count, template);
    }
}
