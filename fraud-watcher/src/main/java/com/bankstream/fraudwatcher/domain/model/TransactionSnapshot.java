package com.bankstream.fraudwatcher.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * A read-only view of a transaction, as seen by fraud-watcher. Deliberately
 * not called "Transaction" — this service doesn't own the transaction, it
 * only observes a copy of it (from Kafka, eventually); "snapshot" makes
 * that one-way relationship explicit.
 */
public record TransactionSnapshot(
        UUID transactionId,
        AccountId accountId,
        TransactionType type,
        Money amount,
        Instant occurredAt
) {
}
