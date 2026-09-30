package com.bankstream.balanceconsumer.infrastructure.adapter.in.web.dto;

import com.bankstream.balanceconsumer.domain.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;


public record ApplyTransactionRequest(

        @NotNull
        UUID transactionId,

        @NotNull
        UUID accountId,

        @NotNull
        TransactionType type,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount,

        @NotNull
        @Pattern(regexp = "[A-Z]{3}")
        String currencyCode
) {
}
