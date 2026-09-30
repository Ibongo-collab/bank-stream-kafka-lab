package com.bankstream.fraudwatcher.application.port.in;

import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import java.util.List;


public interface EvaluateTransactionUseCase {

    List<FraudAlert> evaluate(EvaluateTransactionCommand command);
}
