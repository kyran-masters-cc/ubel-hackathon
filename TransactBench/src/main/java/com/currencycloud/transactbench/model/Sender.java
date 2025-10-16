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
public class Sender {
    @JsonProperty("free_text")
    private String freeText;

    private String name;

    @JsonProperty("ultimate_sender_name")
    private String ultimateSenderName;

    private String address;

    private String country;
}
