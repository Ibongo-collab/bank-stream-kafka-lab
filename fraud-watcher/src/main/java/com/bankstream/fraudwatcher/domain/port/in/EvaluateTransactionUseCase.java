package com.bankstream.fraudwatcher.domain.port.in;

import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import java.util.List;

/**
 * Inbound port. Called today by a temporary REST endpoint
 * ({@code POST /api/v1/fraud/evaluate}); the future Kafka listener will
 * call it for every event on its OWN consumer group — the same topic
 * balance-consumer reads, consumed independently, which is the fan-out
 * pattern this whole 3-service lab exists to demonstrate.
 */
public interface EvaluateTransactionUseCase {

    List<FraudAlert> evaluate(EvaluateTransactionCommand command);
}
