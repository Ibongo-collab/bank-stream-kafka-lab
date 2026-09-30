package com.bankstream.balanceconsumer.application;

import com.bankstream.balanceconsumer.domain.exception.BalanceNotFoundException;
import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;
import com.bankstream.balanceconsumer.domain.model.Money;
import com.bankstream.balanceconsumer.application.port.in.ApplyTransactionCommand;
import com.bankstream.balanceconsumer.application.port.in.ApplyTransactionUseCase;
import com.bankstream.balanceconsumer.application.port.in.GetBalanceUseCase;
import com.bankstream.balanceconsumer.application.port.out.BalanceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
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
                .orElseThrow(() -> new BalanceNotFoundException(accountId));
    }
}
