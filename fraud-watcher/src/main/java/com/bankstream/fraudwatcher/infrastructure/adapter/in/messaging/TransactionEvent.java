package com.bankstream.fraudwatcher.infrastructure.adapter.in.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionEvent(
        String transactionId,
        String accountId,
        String type,
        BigDecimal amount,
        String currencyCode,
        Instant occurredAt
) {
}
