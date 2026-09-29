package com.bankstream.fraudwatcher.domain.rules;

import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import com.bankstream.fraudwatcher.domain.model.TransactionSnapshot;
import java.util.List;
import java.util.Optional;

/**
 * Strategy interface: every concrete rule looks at the current transaction
 * (and, when relevant, the account's recent history) and decides whether
 * to raise an alert. New fraud heuristics are added by writing one more
 * class here and registering it in
 * {@code infrastructure.config.FraudRulesConfiguration} — the application
 * service that runs the rules never changes.
 */
public interface FraudRule {

    Optional<FraudAlert> evaluate(TransactionSnapshot current, List<TransactionSnapshot> recentHistory);
}
