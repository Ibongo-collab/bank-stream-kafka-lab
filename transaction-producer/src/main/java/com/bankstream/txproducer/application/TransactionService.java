package com.bankstream.txproducer.application;

import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.Money;
import com.bankstream.txproducer.domain.model.Transaction;
import com.bankstream.txproducer.application.port.in.ListTransactionsUseCase;
import com.bankstream.txproducer.application.port.in.SubmitTransactionCommand;
import com.bankstream.txproducer.application.port.in.SubmitTransactionUseCase;
import com.bankstream.txproducer.application.port.out.TransactionPublisherPort;
import com.bankstream.txproducer.application.port.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public final class TransactionService implements SubmitTransactionUseCase, ListTransactionsUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final TransactionPublisherPort transactionPublisher;


    @Override
    public Transaction submit(SubmitTransactionCommand command) {
        Money money = Money.of(command.amount(), command.currencyCode());
        Transaction transaction = Transaction.create(command.accountId(), command.type(), money);

        Transaction saved = transactionRepository.save(transaction);
        transactionPublisher.publish(saved);

        return saved;
    }

    @Override
    public List<Transaction> listAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> listByAccount(AccountId accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
