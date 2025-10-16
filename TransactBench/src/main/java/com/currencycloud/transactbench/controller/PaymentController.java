package com.currencycloud.transactbench.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @GetMapping("/providers")
    public List<String> getProviders() {
        return List.of("cfsb", "lhv", "arkea", "dcbank");
    }

    @GetMapping("/payment-rails")
    public List<String> getPaymentRails() {
        return List.of("ach", "fedwire");
    }

    @PostMapping("/submit")
    public Map<String, Object> submitPayment(@RequestBody Map<String, String> request) {
        String provider = request.get("provider");
        String paymentRail = request.get("paymentRail");
        String transactionId = request.get("transactionId");
        String numberOfTransactionsStr = request.get("numberOfTransactions");
        
        // Validate transaction ID as UUID if provided
        if (transactionId != null && !transactionId.trim().isEmpty()) {
            try {
                UUID.fromString(transactionId.trim());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid UUID format for transaction ID: {}", transactionId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Transaction ID must be a valid UUID format (e.g., 123e4567-e89b-12d3-a456-426614174000)");
            }
        }
        
        // If transaction ID is provided, set number of transactions to 1
        int numberOfTransactions;
        if (transactionId != null && !transactionId.trim().isEmpty()) {
            numberOfTransactions = 1;
            log.info("Valid UUID Transaction ID provided: {}, setting numberOfTransactions to 1", transactionId);
        } else {
            numberOfTransactions = numberOfTransactionsStr != null ? 
                Integer.parseInt(numberOfTransactionsStr) : 1;
        }
        
        log.info("provider = {}, paymentRail = {}, numberOfTransactions = {}, transactionId = {}", 
                provider, paymentRail, numberOfTransactions, transactionId);
        
        return Map.of(
            "provider", provider,
            "paymentRail", paymentRail,
            "numberOfTransactions", numberOfTransactions,
            "transactionId", transactionId != null ? transactionId.trim() : "",
            "status", "success"
        );
    }
}