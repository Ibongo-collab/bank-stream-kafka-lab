package com.bankstream.fraudwatcher.infrastructure.adapter.in.messaging;

import com.bankstream.fraudwatcher.application.port.in.EvaluateTransactionCommand;
import com.bankstream.fraudwatcher.application.port.in.EvaluateTransactionUseCase;
import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import com.bankstream.fraudwatcher.domain.model.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaFraudListenerAdapter {

    private final EvaluateTransactionUseCase evaluateTransactionUseCase;

    @KafkaListener(topics = "${app.kafka.topic.transactions}")
    public void onTransaction(TransactionEvent event) {
        log.info("Received transaction {} for account {}", event.transactionId(), event.accountId());

        EvaluateTransactionCommand command = toCommand(event);
        List<FraudAlert> alerts = evaluateTransactionUseCase.evaluate(command);

        if (alerts.isEmpty()) {
            log.info("Transaction {} raised no fraud alert", event.transactionId());
        } else {
            log.warn("Transaction {} raised {} fraud alert(s): {}", event.transactionId(), alerts.size(), alerts);
        }
    }

    public EvaluateTransactionCommand toCommand(TransactionEvent event) {
        return new EvaluateTransactionCommand(
                UUID.fromString(event.transactionId()),
                new AccountId(UUID.fromString(event.accountId())),
                TransactionType.valueOf(event.type()),
                event.amount(),
                event.currencyCode()
        );
    }
}
