package com.bankstream.balanceconsumer.domain.model;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {

    public AccountId {
        Objects.requireNonNull(value, "Account id must not be null");
    }

    public static AccountId of(String raw) {
        return new AccountId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
