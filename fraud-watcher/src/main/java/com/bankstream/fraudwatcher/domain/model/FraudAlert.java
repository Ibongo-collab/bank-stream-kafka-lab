package com.bankstream.fraudwatcher.domain.model;

import java.time.Instant;
import java.util.UUID;

public record FraudAlert(
        UUID id,
        UUID transactionId,
        AccountId accountId,
        String reason,
        Severity severity,
        Instant detectedAt
) {

    public static FraudAlert raise(TransactionSnapshot transaction, String reason, Severity severity) {
        return new FraudAlert(
                UUID.randomUUID(), transaction.transactionId(), transaction.accountId(),
                reason, severity, Instant.now());
    }
}
