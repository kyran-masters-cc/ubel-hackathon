package com.currencycloud.transactbench.controller;

import com.currencycloud.transactbench.dto.TransactionGenerationRequest;
import com.currencycloud.transactbench.dto.TransactionGenerationResponse;
import com.currencycloud.transactbench.model.Transaction;
import com.currencycloud.transactbench.service.TransactionGeneratorService;
import com.currencycloud.transactbench.service.TransactionPublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionGeneratorService generatorService;
    private final TransactionPublisherService publisherService;

    @PostMapping("/generate")
    public ResponseEntity<TransactionGenerationResponse> generateTransactions(
            @Valid @RequestBody TransactionGenerationRequest request) {

        log.info("Received request to generate {} transactions for provider: {}, paymentRail: {}",
                request.getNumberOfMessages(), request.getProvider(), request.getPaymentRail());

        try {
            // Generate transactions
            List<Transaction> transactions = generatorService.generateTransactions(
                    request.getProvider(),
                    request.getPaymentRail(),
                    request.getNumberOfMessages()
            );

            // Publish to RabbitMQ
            publisherService.publishTransactions(transactions);

            // Extract transaction IDs for response
            List<String> transactionIds = transactions.stream()
                    .map(Transaction::getId)
                    .collect(Collectors.toList());

            TransactionGenerationResponse response = new TransactionGenerationResponse(transactionIds);

            log.info("Successfully generated and published {} transactions", transactionIds.size());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid configuration for provider: {}, paymentRail: {}",
                    request.getProvider(), request.getPaymentRail(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            log.error("Error generating transactions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
