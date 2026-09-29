package com.bankstream.fraudwatcher.domain.rules;

import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import com.bankstream.fraudwatcher.domain.model.Money;
import com.bankstream.fraudwatcher.domain.model.Severity;
import com.bankstream.fraudwatcher.domain.model.TransactionSnapshot;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

/**
 * Flags any single transaction above a configured threshold. The
 * threshold is a plain constructor argument, not a Spring
 * {@code @Value} — this class has zero framework dependency and is
 * trivially unit-testable with `new HighAmountRule(...)`. Binding it to
 * {@code application.yml} is infrastructure's job
 * ({@code FraudRulesConfiguration}), not this class's.
 */
public class HighAmountRule implements FraudRule {

    private final Money threshold;

    public HighAmountRule(BigDecimal thresholdAmount, String currencyCode) {
        this.threshold = new Money(thresholdAmount, Currency.getInstance(currencyCode));
    }

    @Override
    public Optional<FraudAlert> evaluate(TransactionSnapshot current, List<TransactionSnapshot> recentHistory) {
        if (current.amount().isGreaterThan(threshold)) {
            return Optional.of(FraudAlert.raise(
                    current,
                    "Amount " + current.amount() + " exceeds the single-transaction threshold of " + threshold,
                    Severity.HIGH));
        }
        return Optional.empty();
    }
}
