package com.bankstream.txproducer.infrastructure.adapter.in.web.mapper;

import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.Transaction;
import com.bankstream.txproducer.domain.port.in.SubmitTransactionCommand;
import com.bankstream.txproducer.infrastructure.adapter.in.web.dto.SubmitTransactionRequest;
import com.bankstream.txproducer.infrastructure.adapter.in.web.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct handles the mechanical field-by-field mapping; the two
 * {@code default} methods handle the parts that aren't 1:1 (wrapping a raw
 * UUID into the {@code AccountId} value object, flattening {@code Money}
 * back into amount+currency for the response).
 */
@Mapper(componentModel = "spring")
public interface TransactionWebMapper {

    @Mapping(target = "accountId", expression = "java(com.bankstream.txproducer.domain.model.AccountId.of(request.accountId().toString()))")
    SubmitTransactionCommand toCommand(SubmitTransactionRequest request);

    default TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.id().value(),
                transaction.accountId().value(),
                transaction.type(),
                transaction.money().amount(),
                transaction.money().currency().getCurrencyCode(),
                transaction.occurredAt()
        );
    }

    default AccountId toAccountId(java.util.UUID raw) {
        return new AccountId(raw);
    }
}
