package com.bankstream.fraudwatcher.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.Money;
import com.bankstream.fraudwatcher.domain.model.TransactionSnapshot;
import com.bankstream.fraudwatcher.domain.model.TransactionType;
import com.bankstream.fraudwatcher.domain.rules.HighAmountRule;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class HighAmountRuleTest {

    private final HighAmountRule rule = new HighAmountRule(new BigDecimal("1000000"), "XAF");

    @Test
    void raisesAnAlertWhenAmountExceedsTheThreshold() {
        TransactionSnapshot snapshot = snapshotOf(new BigDecimal("1500000"));

        assertThat(rule.evaluate(snapshot, List.of())).isPresent();
    }

    @Test
    void staysSilentWhenAmountIsBelowTheThreshold() {
        TransactionSnapshot snapshot = snapshotOf(new BigDecimal("500"));

        assertThat(rule.evaluate(snapshot, List.of())).isEmpty();
    }

    private TransactionSnapshot snapshotOf(BigDecimal amount) {
        return new TransactionSnapshot(
                UUID.randomUUID(),
                new AccountId(UUID.randomUUID()),
                TransactionType.WITHDRAWAL,
                Money.of(amount, "XAF"),
                Instant.now());
    }
}
