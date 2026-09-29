package com.bankstream.balanceconsumer.domain.port.in;

import com.bankstream.balanceconsumer.domain.model.Balance;

/**
 * Inbound port. Today it's called by a temporary REST endpoint
 * ({@code POST /api/v1/balances/apply}); once the Kafka part of the lab
 * is wired, a {@code @KafkaListener}-based adapter will call this exact
 * same method for every {@code transactions.completed} event, and the
 * REST endpoint can be deleted without this use case changing at all.
 */
public interface ApplyTransactionUseCase {

    Balance apply(ApplyTransactionCommand command);
}
