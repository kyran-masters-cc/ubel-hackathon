package com.currencycloud.transactbench.config;

import lombok.Data;

@Data
public class ProviderConfig {
    private String currency;
    private String destinationAccountNumber;
    private String senderName;
    private String country;
}