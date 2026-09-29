package com.bankstream.txproducer.domain.model;

import com.bankstream.txproducer.domain.exception.InvalidTransactionException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * A monetary amount tied to a currency. Deliberately not a bare
 * {@code BigDecimal} field on {@link Transaction} — bundling the currency
 * with the amount is what stops "amount in EUR compared to amount in XAF"
 * bugs from ever compiling.
 */
public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(currency, "Currency must not be null");
        if (amount.signum() <= 0) {
            throw new InvalidTransactionException("Amount must be strictly positive, got " + amount);
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, Currency.getInstance(currencyCode));
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }
}
