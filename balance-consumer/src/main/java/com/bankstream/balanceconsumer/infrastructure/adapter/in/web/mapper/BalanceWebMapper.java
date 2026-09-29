package com.bankstream.balanceconsumer.infrastructure.adapter.in.web.mapper;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionCommand;
import com.bankstream.balanceconsumer.infrastructure.adapter.in.web.dto.ApplyTransactionRequest;
import com.bankstream.balanceconsumer.infrastructure.adapter.in.web.dto.BalanceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceWebMapper {

    @Mapping(target = "accountId", expression = "java(new com.bankstream.balanceconsumer.domain.model.AccountId(request.accountId()))")
    ApplyTransactionCommand toCommand(ApplyTransactionRequest request);

    default BalanceResponse toResponse(Balance balance) {
        return new BalanceResponse(
                balance.accountId().value(),
                balance.amount().amount(),
                balance.amount().currency().getCurrencyCode(),
                balance.lastUpdatedAt()
        );
    }

    default AccountId toAccountId(java.util.UUID raw) {
        return new AccountId(raw);
    }
}
