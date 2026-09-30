package com.bankstream.fraudwatcher.infrastructure.adapter.in.web.mapper;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import com.bankstream.fraudwatcher.application.port.in.EvaluateTransactionCommand;
import com.bankstream.fraudwatcher.infrastructure.adapter.in.web.dto.EvaluateTransactionRequest;
import com.bankstream.fraudwatcher.infrastructure.adapter.in.web.dto.FraudAlertResponse;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(componentModel = "spring")
public interface FraudWebMapper {

    @Mapping(target = "accountId", expression = "java(new com.bankstream.fraudwatcher.domain.model.AccountId(request.accountId()))")
    EvaluateTransactionCommand toCommand(EvaluateTransactionRequest request);

    default FraudAlertResponse toResponse(FraudAlert alert) {
        return new FraudAlertResponse(
                alert.id(),
                alert.transactionId(),
                alert.accountId().value(),
                alert.reason(),
                alert.severity().name(),
                alert.detectedAt()
        );
    }

    default AccountId toAccountId(java.util.UUID raw) {
        return new AccountId(raw);
    }
}
