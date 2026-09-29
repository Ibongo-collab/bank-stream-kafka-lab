package com.bankstream.txproducer.domain.model;

import java.util.Objects;
import java.util.UUID;

public record TransactionId(UUID value) {

    public TransactionId {
        Objects.requireNonNull(value, "Transaction id must not be null");
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
