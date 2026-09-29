package com.bankstream.txproducer.infrastructure.adapter.in.web.dto;

import com.bankstream.txproducer.domain.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;

public record SubmitTransactionRequest(

        @NotNull(message = "accountId is required")
        UUID accountId,

        @NotNull(message = "type is required")
        TransactionType type,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be strictly positive")
        BigDecimal amount,

        @NotNull(message = "currencyCode is required")
        @Pattern(regexp = "[A-Z]{3}", message = "currencyCode must be a 3-letter ISO code, e.g. XAF")
        String currencyCode
) {
}
