package com.currencycloud.transactbench.service;

import com.currencycloud.transactbench.config.ProviderConfig;
import com.currencycloud.transactbench.config.TransactionConfigProperties;
import com.currencycloud.transactbench.model.Account;
import com.currencycloud.transactbench.model.Sender;
import com.currencycloud.transactbench.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionGeneratorService {
    private final TransactionConfigProperties configProperties;
    private static final Random random = new Random();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Transaction> generateTransactions(String provider, String paymentRail, int count, UUID transactionId) {
        String transactionIdStr = transactionId != null ? transactionId.toString() : null;
        log.info("Generating {} transactions for provider: {}, paymentRail: {}", count, provider, paymentRail);

        ProviderConfig config = configProperties.getConfig(provider, paymentRail);
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Transaction transaction = buildTransaction(provider, paymentRail, config, transactionIdStr);
            transactions.add(transaction);
        }

        log.info("Generated {} transactions successfully", transactions.size());
        return transactions;
    }

    private Transaction buildTransaction(String provider, String paymentRail, ProviderConfig config, String transactionId) {
        String actualTransactionId = transactionId != null ? transactionId : UUID.randomUUID().toString();

        return Transaction.builder()
                .id(actualTransactionId)
                .version(2)
                .creditDebit("credit")
                .amount(generateRandomAmount())
                .currency(config.getCurrency())
                .valueDate(generateFutureDate())
                .trackingId(actualTransactionId)
                .reference(generateReference())
                .paymentRail(paymentRail)
                .provider(provider)
                .originAccount(buildOriginAccount())
                .destinationAccount(buildDestinationAccount(config.getDestinationAccountNumber()))
                .sender(buildSender(config.getSenderName(), config.getCountry()))
                .transactionContent(null)
                .build();
    }

    private int generateRandomAmount() {
        // Generate random amount between 100 and 10000
        return 100 + random.nextInt(9901);
    }

    private String generateFutureDate() {
        // Generate a date between 1 and 90 days in the future
        int daysInFuture = 1 + random.nextInt(90);
        return LocalDate.now().plusDays(daysInFuture).format(DATE_FORMATTER);
    }

    private String generateReference() {
        return "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Account buildOriginAccount() {
        return Account.builder()
                .accountNumber("FR26TCCL20786956994877")
                .bic("CMBRFR2BARK")
                .iban("FR26TCCL20786956994877")
                .build();
    }

    private Account buildDestinationAccount(String accountNumber) {
        return Account.builder()
                .accountNumber(accountNumber)
                .build();
    }

    private Sender buildSender(String senderName, String country) {
        return Sender.builder()
                .freeText("unstructuredOne, unstructuredTwo")
                .name(senderName)
                .ultimateSenderName("ultimateDebtorName")
                .address("address line1, address line2, address line3, address line4")
                .country(country)
                .build();
    }
}
