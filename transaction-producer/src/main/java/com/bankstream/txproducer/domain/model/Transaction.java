package com.bankstream.txproducer.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * The aggregate root of this service: an immutable record of a submitted
 */
public record Transaction(
        TransactionId id,
        AccountId accountId,
        TransactionType type,
        Money money,
        Instant occurredAt
) {

    public Transaction {
        Objects.requireNonNull(id, "Transaction id must not be null");
        Objects.requireNonNull(accountId, "Account id must not be null");
        Objects.requireNonNull(type, "Transaction type must not be null");
        Objects.requireNonNull(money, "Money must not be null");
        Objects.requireNonNull(occurredAt, "Occurred-at must not be null");
    }

    public static Transaction create(AccountId accountId, TransactionType type, Money money) {
        return new Transaction(TransactionId.generate(), accountId, type, money, Instant.now());
    }
}
