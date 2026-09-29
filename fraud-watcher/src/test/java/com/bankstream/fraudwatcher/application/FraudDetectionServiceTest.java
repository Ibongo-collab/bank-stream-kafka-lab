package com.bankstream.fraudwatcher.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.TransactionType;
import com.bankstream.fraudwatcher.domain.port.in.EvaluateTransactionCommand;
import com.bankstream.fraudwatcher.domain.rules.HighAmountRule;
import com.bankstream.fraudwatcher.domain.rules.VelocityRule;
import com.bankstream.fraudwatcher.infrastructure.adapter.out.persistence.InMemoryFraudAlertRepositoryAdapter;
import com.bankstream.fraudwatcher.infrastructure.adapter.out.persistence.InMemoryTransactionHistoryAdapter;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FraudDetectionServiceTest {

    @Test
    void aHighAmountTransactionRaisesExactlyOneAlert() {
        FraudDetectionService service = new FraudDetectionService(
                List.of(new HighAmountRule(new BigDecimal("1000000"), "XAF"), new VelocityRule(100)),
                new InMemoryFraudAlertRepositoryAdapter(),
                new InMemoryTransactionHistoryAdapter(),
                Duration.ofMinutes(1));

        AccountId accountId = new AccountId(UUID.randomUUID());
        var alerts = service.evaluate(new EvaluateTransactionCommand(
                UUID.randomUUID(), accountId, TransactionType.WITHDRAWAL, new BigDecimal("5000000"), "XAF"));

        assertThat(alerts).hasSize(1);
        assertThat(service.listByAccount(accountId)).hasSize(1);
    }

    @Test
    void repeatedSmallTransactionsTriggerTheVelocityRuleOnce() {
        FraudDetectionService service = new FraudDetectionService(
                List.of(new VelocityRule(3)),
                new InMemoryFraudAlertRepositoryAdapter(),
                new InMemoryTransactionHistoryAdapter(),
                Duration.ofMinutes(1));

        AccountId accountId = new AccountId(UUID.randomUUID());
        for (int i = 0; i < 3; i++) {
            service.evaluate(new EvaluateTransactionCommand(
                    UUID.randomUUID(), accountId, TransactionType.DEPOSIT, new BigDecimal("10"), "XAF"));
        }

        var alerts = service.evaluate(new EvaluateTransactionCommand(
                UUID.randomUUID(), accountId, TransactionType.DEPOSIT, new BigDecimal("10"), "XAF"));

        assertThat(alerts).hasSize(1);
    }
}
