package com.bankstream.balanceconsumer.application;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;
import com.bankstream.balanceconsumer.domain.model.Money;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionCommand;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionUseCase;
import com.bankstream.balanceconsumer.domain.port.in.GetBalanceUseCase;
import com.bankstream.balanceconsumer.domain.port.out.BalanceRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Currency;

@RequiredArgsConstructor
public final class BalanceService implements ApplyTransactionUseCase, GetBalanceUseCase {

    private final BalanceRepositoryPort balanceRepository;

    @Override
    public Balance apply(ApplyTransactionCommand command) {
        Currency currency = Currency.getInstance(command.currencyCode());
        Balance current = balanceRepository.findByAccountId(command.accountId())
                .orElseGet(() -> Balance.zero(command.accountId(), currency));

        Money transactionAmount = Money.of(command.amount(), command.currencyCode());
        Balance updated = current.applyTransaction(command.type(), transactionAmount);

        return balanceRepository.save(updated);
    }

    @Override
    public Balance getBalance(AccountId accountId) {
        return balanceRepository.findByAccountId(accountId)
                .orElseThrow(() -> new com.bankstream.balanceconsumer.domain.exception.BalanceNotFoundException(accountId));
    }
}
