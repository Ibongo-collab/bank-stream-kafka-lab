package com.bankstream.balanceconsumer.application;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;
import com.bankstream.balanceconsumer.domain.model.Money;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionCommand;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionUseCase;
import com.bankstream.balanceconsumer.domain.port.in.GetBalanceUseCase;
import com.bankstream.balanceconsumer.domain.port.out.BalanceRepositoryPort;
import java.util.Currency;
import java.util.Objects;

/**
 * Plain Java, no Spring — wired by {@code BeanConfiguration} in
 * infrastructure. The compare-and-apply here (read current balance,
 * compute the new one, save it) is exactly what will need an idempotency
 * check added once Kafka can redeliver the same event (at-least-once
 * delivery): a natural next step for this class, not a redesign.
 */
public final class BalanceService implements ApplyTransactionUseCase, GetBalanceUseCase {

    private final BalanceRepositoryPort balanceRepository;

    public BalanceService(BalanceRepositoryPort balanceRepository) {
        this.balanceRepository = Objects.requireNonNull(balanceRepository);
    }

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
