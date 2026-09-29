package com.bankstream.balanceconsumer.infrastructure.adapter.in.web.mapper;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.TransactionType;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionCommand;
import com.bankstream.balanceconsumer.infrastructure.adapter.in.web.dto.ApplyTransactionRequest;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-28T23:06:58+0000",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class BalanceWebMapperImpl implements BalanceWebMapper {

    @Override
    public ApplyTransactionCommand toCommand(ApplyTransactionRequest request) {
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

        AccountId accountId = new com.bankstream.balanceconsumer.domain.model.AccountId(request.accountId());

        ApplyTransactionCommand applyTransactionCommand = new ApplyTransactionCommand( transactionId, accountId, type, amount, currencyCode );

        return applyTransactionCommand;
    }
}
