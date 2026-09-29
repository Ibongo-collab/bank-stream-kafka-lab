package com.bankstream.balanceconsumer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bankstream.balanceconsumer.domain.exception.InsufficientFundsException;
import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;
import com.bankstream.balanceconsumer.domain.model.Money;
import com.bankstream.balanceconsumer.domain.model.TransactionType;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BalanceTest {

    private final Currency xaf = Currency.getInstance("XAF");
    private final AccountId accountId = new AccountId(UUID.randomUUID());

    @Test
    void depositIncreasesTheBalance() {
        Balance balance = Balance.zero(accountId, xaf);

        Balance updated = balance.applyTransaction(TransactionType.DEPOSIT, Money.of(new BigDecimal("100"), "XAF"));

        assertThat(updated.amount().amount()).isEqualByComparingTo("100.00");
    }

    @Test
    void withdrawalDecreasesTheBalanceWhenFundsAreSufficient() {
        Balance balance = Balance.zero(accountId, xaf)
                .applyTransaction(TransactionType.DEPOSIT, Money.of(new BigDecimal("100"), "XAF"));

        Balance updated = balance.applyTransaction(TransactionType.WITHDRAWAL, Money.of(new BigDecimal("40"), "XAF"));

        assertThat(updated.amount().amount()).isEqualByComparingTo("60.00");
    }

    @Test
    void withdrawalBeyondBalanceIsRejected() {
        Balance balance = Balance.zero(accountId, xaf);

        assertThatThrownBy(() ->
                balance.applyTransaction(TransactionType.WITHDRAWAL, Money.of(new BigDecimal("10"), "XAF")))
                .isInstanceOf(InsufficientFundsException.class);
    }
}
