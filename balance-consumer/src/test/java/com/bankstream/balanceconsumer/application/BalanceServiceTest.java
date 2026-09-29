package com.bankstream.balanceconsumer.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;
import com.bankstream.balanceconsumer.domain.model.TransactionType;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionCommand;
import com.bankstream.balanceconsumer.infrastructure.adapter.out.persistence.InMemoryBalanceRepositoryAdapter;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BalanceServiceTest {

    @Test
    void applyingTwoDepositsAccumulatesTheBalance() {
        BalanceService service = new BalanceService(new InMemoryBalanceRepositoryAdapter());
        AccountId accountId = new AccountId(UUID.randomUUID());

        service.apply(new ApplyTransactionCommand(UUID.randomUUID(), accountId, TransactionType.DEPOSIT, new BigDecimal("100"), "XAF"));
        Balance result = service.apply(new ApplyTransactionCommand(UUID.randomUUID(), accountId, TransactionType.DEPOSIT, new BigDecimal("50"), "XAF"));

        assertThat(result.amount().amount()).isEqualByComparingTo("150.00");
        assertThat(service.getBalance(accountId)).isEqualTo(result);
    }
}
