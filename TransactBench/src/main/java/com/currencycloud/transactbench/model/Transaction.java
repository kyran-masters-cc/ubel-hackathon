package com.currencycloud.transactbench.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id;

    private Integer version;

    @JsonProperty("credit_debit")
    private String creditDebit;

    private Integer amount;

    private String currency;

    @JsonProperty("value_date")
    private String valueDate;

    @JsonProperty("tracking_id")
    private String trackingId;

    private String reference;

    @JsonProperty("payment_rail")
    private String paymentRail;

    private String provider;

    @JsonProperty("origin_account")
    private Account originAccount;

    @JsonProperty("destination_account")
    private Account destinationAccount;

    private Sender sender;

    @JsonProperty("transaction_content")
    private Object transactionContent;
}
