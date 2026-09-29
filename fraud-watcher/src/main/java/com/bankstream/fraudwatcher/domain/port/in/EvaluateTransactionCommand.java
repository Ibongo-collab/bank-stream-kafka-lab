package com.bankstream.fraudwatcher.domain.port.in;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;

public record EvaluateTransactionCommand(
        UUID transactionId,
        AccountId accountId,
        TransactionType type,
        BigDecimal amount,
        String currencyCode
) {
}
