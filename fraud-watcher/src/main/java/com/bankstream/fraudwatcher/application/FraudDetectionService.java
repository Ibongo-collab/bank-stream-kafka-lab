package com.bankstream.fraudwatcher.application;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import com.bankstream.fraudwatcher.domain.model.Money;
import com.bankstream.fraudwatcher.domain.model.TransactionSnapshot;
import com.bankstream.fraudwatcher.application.port.in.EvaluateTransactionCommand;
import com.bankstream.fraudwatcher.application.port.in.EvaluateTransactionUseCase;
import com.bankstream.fraudwatcher.application.port.in.ListAlertsUseCase;
import com.bankstream.fraudwatcher.application.port.out.FraudAlertRepositoryPort;
import com.bankstream.fraudwatcher.application.port.out.TransactionHistoryPort;
import com.bankstream.fraudwatcher.domain.rules.FraudRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public final class FraudDetectionService implements EvaluateTransactionUseCase, ListAlertsUseCase {

    private final List<FraudRule> rules;
    private final FraudAlertRepositoryPort alertRepository;
    private final TransactionHistoryPort transactionHistory;
    private final Duration velocityWindow;


    @Override
    public List<FraudAlert> evaluate(EvaluateTransactionCommand command) {
        TransactionSnapshot snapshot = new TransactionSnapshot(
                command.transactionId(),
                command.accountId(),
                command.type(),
                Money.of(command.amount(), command.currencyCode()),
                Instant.now());

        List<TransactionSnapshot> recentHistory =
                transactionHistory.findRecentByAccount(command.accountId(), velocityWindow);

        List<FraudAlert> raised = rules.stream()
                .map(rule -> rule.evaluate(snapshot, recentHistory))
                .flatMap(java.util.Optional::stream)
                .map(alertRepository::save)
                .toList();

        // Recorded after evaluation so the current transaction doesn't count
        // against itself in the velocity check above.
        transactionHistory.record(snapshot);

        return raised;
    }

    @Override
    public List<FraudAlert> listAll() {
        return alertRepository.findAll();
    }

    @Override
    public List<FraudAlert> listByAccount(AccountId accountId) {
        return alertRepository.findByAccountId(accountId);
    }
}
