package com.currencycloud.transactbench.service;

import com.currencycloud.transactbench.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionPublisherService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name:transaction-exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key:new_transaction_message}")
    private String routingKey;

    public void publishTransactions(List<Transaction> transactions) {
        log.info("Publishing {} transactions to RabbitMQ", transactions.size());

        for (Transaction transaction : transactions) {
            try {
                rabbitTemplate.convertAndSend(exchangeName, routingKey, transaction);
                log.debug("Published transaction with ID: {}", transaction.getId());
            } catch (Exception e) {
                log.error("Failed to publish transaction with ID: {}", transaction.getId(), e);
                throw new RuntimeException("Failed to publish transaction: " + transaction.getId(), e);
            }
        }

        log.info("Successfully published {} transactions", transactions.size());
    }
}
