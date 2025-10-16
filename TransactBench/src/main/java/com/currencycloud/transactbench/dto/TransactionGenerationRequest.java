package com.currencycloud.transactbench.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionGenerationRequest {
    @NotBlank(message = "Provider is required")
    private String provider;

    @NotBlank(message = "Payment rail is required")
    private String paymentRail;

    @NotNull(message = "Number of messages is required")
    @Min(value = 1, message = "Number of messages must be at least 1")
    private Integer numberOfMessages;

    private UUID transactionId;
}
