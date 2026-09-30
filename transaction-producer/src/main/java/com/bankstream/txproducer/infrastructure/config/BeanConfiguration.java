package com.bankstream.txproducer.infrastructure.config;


import com.bankstream.txproducer.application.port.out.TransactionRepositoryPort;
import com.bankstream.txproducer.infrastructure.adapter.out.persistence.InMemoryTransactionRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfiguration {

    @Bean
    public TransactionRepositoryPort transactionRepositoryPort() {
        return new InMemoryTransactionRepositoryAdapter();
    }
}
