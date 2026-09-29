package com.bankstream.balanceconsumer.infrastructure.config;

import com.bankstream.balanceconsumer.application.BalanceService;
import com.bankstream.balanceconsumer.domain.port.out.BalanceRepositoryPort;
import com.bankstream.balanceconsumer.infrastructure.adapter.out.persistence.InMemoryBalanceRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public BalanceRepositoryPort balanceRepositoryPort() {
        return new InMemoryBalanceRepositoryAdapter();
    }

    @Bean
    public BalanceService balanceService(BalanceRepositoryPort balanceRepositoryPort) {
        return new BalanceService(balanceRepositoryPort);
    }

    // TODO(kafka-lab): once wired, add a KafkaTransactionListenerAdapter
    // bean here that consumes "transactions.completed" and calls
    // BalanceService#apply for every event — replacing the temporary
    // POST /api/v1/balances/apply endpoint.
}
