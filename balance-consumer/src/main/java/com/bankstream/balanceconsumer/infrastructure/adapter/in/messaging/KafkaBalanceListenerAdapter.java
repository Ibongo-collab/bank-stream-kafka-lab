package com.bankstream.balanceconsumer.infrastructure.adapter.in.messaging;

import com.bankstream.balanceconsumer.application.port.in.ApplyTransactionCommand;
import com.bankstream.balanceconsumer.application.port.in.ApplyTransactionUseCase;
import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaBalanceListenerAdapter {

    private final ApplyTransactionUseCase applyTransactionUseCase;

    @KafkaListener(topics = "${app.kafka.topic.transactions}")
    public void onTransaction(TransactionEvent event) {
        log.info("Received transaction {} for account {}", event.transactionId(), event.accountId());

        ApplyTransactionCommand command = toCommand(event);
        applyTransactionUseCase.apply(command);

        log.info("Applied transaction {} to the balance of account {}", event.transactionId(), event.accountId());
    }

    private ApplyTransactionCommand toCommand(TransactionEvent event) {
        return new ApplyTransactionCommand(
                UUID.fromString(event.transactionId()),
                AccountId.of(event.accountId()),
                TransactionType.valueOf(event.type()),
                event.amount(),
                event.currencyCode());
    }

}
