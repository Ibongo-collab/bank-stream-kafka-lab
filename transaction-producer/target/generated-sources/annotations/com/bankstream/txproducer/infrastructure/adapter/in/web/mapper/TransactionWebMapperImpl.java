package com.bankstream.txproducer.infrastructure.adapter.in.web.mapper;

import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.TransactionType;
import com.bankstream.txproducer.application.port.in.SubmitTransactionCommand;
import com.bankstream.txproducer.infrastructure.adapter.in.web.dto.SubmitTransactionRequest;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-29T01:07:45+0000",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class TransactionWebMapperImpl implements TransactionWebMapper {

    @Override
    public SubmitTransactionCommand toCommand(SubmitTransactionRequest request) {
        if ( request == null ) {
            return null;
        }

        TransactionType type = null;
        BigDecimal amount = null;
        String currencyCode = null;

        type = request.type();
        amount = request.amount();
        currencyCode = request.currencyCode();

        AccountId accountId = com.bankstream.txproducer.domain.model.AccountId.of(request.accountId().toString());

        SubmitTransactionCommand submitTransactionCommand = new SubmitTransactionCommand( accountId, type, amount, currencyCode );

        return submitTransactionCommand;
    }
}
