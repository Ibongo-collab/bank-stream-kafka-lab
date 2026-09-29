package com.bankstream.fraudwatcher.infrastructure.adapter.in.web.mapper;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.TransactionType;
import com.bankstream.fraudwatcher.domain.port.in.EvaluateTransactionCommand;
import com.bankstream.fraudwatcher.infrastructure.adapter.in.web.dto.EvaluateTransactionRequest;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-28T23:16:45+0000",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class FraudWebMapperImpl implements FraudWebMapper {

    @Override
    public EvaluateTransactionCommand toCommand(EvaluateTransactionRequest request) {
        if ( request == null ) {
            return null;
        }

        UUID transactionId = null;
        TransactionType type = null;
        BigDecimal amount = null;
        String currencyCode = null;

        transactionId = request.transactionId();
        type = request.type();
        amount = request.amount();
        currencyCode = request.currencyCode();

        AccountId accountId = new com.bankstream.fraudwatcher.domain.model.AccountId(request.accountId());

        EvaluateTransactionCommand evaluateTransactionCommand = new EvaluateTransactionCommand( transactionId, accountId, type, amount, currencyCode );

        return evaluateTransactionCommand;
    }
}
