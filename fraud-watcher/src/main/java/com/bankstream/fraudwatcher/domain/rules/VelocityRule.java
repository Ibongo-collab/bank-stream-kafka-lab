package com.bankstream.fraudwatcher.domain.rules;

import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import com.bankstream.fraudwatcher.domain.model.Severity;
import com.bankstream.fraudwatcher.domain.model.TransactionSnapshot;
import java.util.List;
import java.util.Optional;

/**
 * Flags an account making too many transactions in a short window
 * ("velocity" — the classic first fraud signal: a compromised account
 * suddenly transacting far more often than usual). The window itself is
 * NOT enforced here: {@code recentHistory} is expected to already be
 * filtered to the relevant window by whoever calls this rule (the
 * application service, via {@code TransactionHistoryPort}) — this class
 * only knows "how many is too many", not "how far back to look".
 */
public class VelocityRule implements FraudRule {

    private final int maxTransactionsInWindow;

    public VelocityRule(int maxTransactionsInWindow) {
        this.maxTransactionsInWindow = maxTransactionsInWindow;
    }

    @Override
    public Optional<FraudAlert> evaluate(TransactionSnapshot current, List<TransactionSnapshot> recentHistory) {
        // +1 to count the current transaction itself alongside its recent history.
        int countInWindow = recentHistory.size() + 1;

        if (countInWindow > maxTransactionsInWindow) {
            return Optional.of(FraudAlert.raise(
                    current,
                    "Account made " + countInWindow + " transactions in the monitored window "
                            + "(limit: " + maxTransactionsInWindow + ")",
                    Severity.MEDIUM));
        }
        return Optional.empty();
    }
}
