package com.bankstream.txproducer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bankstream.txproducer.domain.exception.InvalidTransactionException;
import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.Transaction;
import com.bankstream.txproducer.domain.model.TransactionType;
import com.bankstream.txproducer.domain.port.in.SubmitTransactionCommand;
import com.bankstream.txproducer.domain.port.out.TransactionPublisherPort;
import com.bankstream.txproducer.domain.port.out.TransactionRepositoryPort;
import com.bankstream.txproducer.infrastructure.adapter.out.persistence.InMemoryTransactionRepositoryAdapter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Pure unit test — no {@code @SpringBootTest}, no Spring context at all.
 * That's the payoff of keeping the application layer framework-free: this
 * test runs in milliseconds and only fails when the actual business logic
 * is wrong, never because of a misconfigured bean.
 */
class TransactionServiceTest {

    @Test
    void submitPersistsAndPublishesTheTransaction() {
        TransactionRepositoryPort repository = new InMemoryTransactionRepositoryAdapter();
        List<Transaction> published = new ArrayList<>();
        TransactionPublisherPort publisher = published::add;

        TransactionService service = new TransactionService(repository, publisher);

        AccountId accountId = AccountId.generate();
        SubmitTransactionCommand command = new SubmitTransactionCommand(
                accountId, TransactionType.DEPOSIT, new BigDecimal("100.00"), "XAF");

        Transaction result = service.submit(command);

        assertThat(result.accountId()).isEqualTo(accountId);
        assertThat(result.type()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(repository.findByAccountId(accountId)).containsExactly(result);
        assertThat(published).containsExactly(result);
    }

    @Test
    void submitRejectsANonPositiveAmount() {
        TransactionService service = new TransactionService(
                new InMemoryTransactionRepositoryAdapter(), tx -> { });

        SubmitTransactionCommand command = new SubmitTransactionCommand(
                AccountId.generate(), TransactionType.WITHDRAWAL, new BigDecimal("0.00"), "XAF");

        assertThatThrownBy(() -> service.submit(command))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    void listByAccountOnlyReturnsThatAccountsTransactions() {
        TransactionRepositoryPort repository = new InMemoryTransactionRepositoryAdapter();
        TransactionService service = new TransactionService(repository, tx -> { });

        AccountId accountA = AccountId.generate();
        AccountId accountB = AccountId.generate();

        service.submit(new SubmitTransactionCommand(accountA, TransactionType.DEPOSIT, new BigDecimal("50"), "XAF"));
        service.submit(new SubmitTransactionCommand(accountB, TransactionType.DEPOSIT, new BigDecimal("75"), "XAF"));

        assertThat(service.listByAccount(accountA)).hasSize(1);
        assertThat(service.listByAccount(accountA).get(0).accountId()).isEqualTo(accountA);
    }
}
