package com.bankstream.fraudwatcher.domain.model;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {

    public AccountId {
        Objects.requireNonNull(value, "Account id must not be null");
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
