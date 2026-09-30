package com.bankstream.fraudwatcher.domain.model;

import java.time.Instant;
import java.util.UUID;


public record TransactionSnapshot(
        UUID transactionId,
        AccountId accountId,
        TransactionType type,
        Money amount,
        Instant occurredAt
) {
}
