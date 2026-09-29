package com.bankstream.balanceconsumer.domain.model;

import com.bankstream.balanceconsumer.domain.exception.InsufficientFundsException;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;

/**
 * The aggregate this whole service exists to maintain. Immutable by
 * design: applying a transaction produces a new {@code Balance} rather
 * than mutating this one — which is what makes
 * {@link #applyTransaction(TransactionType, Money)} trivially unit
 * testable and safe to call concurrently without synchronization tricks
 * (the caller/repository owns the compare-and-swap).
 */
public record Balance(AccountId accountId, Money amount, Instant lastUpdatedAt) {

    public Balance {
        Objects.requireNonNull(accountId, "Account id must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(lastUpdatedAt, "Last-updated-at must not be null");
    }

    public static Balance zero(AccountId accountId, Currency currency) {
        return new Balance(accountId, Money.zero(currency), Instant.now());
    }

    /**
     * Applies a transaction to this balance and returns the resulting
     * balance. A withdrawal that would take the account negative is
     * rejected here, in the domain — not as a validation annotation on a
     * DTO, and not as an afterthought in the Kafka listener that will
     * eventually call this.
     */
    public Balance applyTransaction(TransactionType type, Money transactionAmount) {
        Money newAmount = switch (type) {
            case DEPOSIT -> amount.add(transactionAmount);
            case WITHDRAWAL -> {
                if (amount.isLessThan(transactionAmount)) {
                    throw new InsufficientFundsException(accountId);
                }
                yield amount.subtract(transactionAmount);
            }
        };

        return new Balance(accountId, newAmount, Instant.now());
    }
}
