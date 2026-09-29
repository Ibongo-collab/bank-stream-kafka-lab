package com.bankstream.fraudwatcher.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.Money;
import com.bankstream.fraudwatcher.domain.model.TransactionSnapshot;
import com.bankstream.fraudwatcher.domain.model.TransactionType;
import com.bankstream.fraudwatcher.domain.rules.VelocityRule;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class VelocityRuleTest {

    private final VelocityRule rule = new VelocityRule(3);
    private final AccountId accountId = new AccountId(UUID.randomUUID());

    @Test
    void staysSilentWhenUnderTheLimit() {
        TransactionSnapshot current = snapshot();
        List<TransactionSnapshot> history = List.of(snapshot(), snapshot());

        assertThat(rule.evaluate(current, history)).isEmpty();
    }

    @Test
    void raisesAnAlertWhenAtOrOverTheLimit() {
        TransactionSnapshot current = snapshot();
        List<TransactionSnapshot> history = List.of(snapshot(), snapshot(), snapshot());

        assertThat(rule.evaluate(current, history)).isPresent();
    }

    private TransactionSnapshot snapshot() {
        return new TransactionSnapshot(
                UUID.randomUUID(), accountId, TransactionType.DEPOSIT,
                Money.of(new BigDecimal("10"), "XAF"), Instant.now());
    }
}
