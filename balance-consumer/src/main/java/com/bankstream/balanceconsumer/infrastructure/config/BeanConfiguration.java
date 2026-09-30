package com.bankstream.balanceconsumer.infrastructure.config;

import com.bankstream.balanceconsumer.application.port.out.BalanceRepositoryPort;
import com.bankstream.balanceconsumer.infrastructure.adapter.out.persistence.InMemoryBalanceRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public BalanceRepositoryPort balanceRepositoryPort() {
        return new InMemoryBalanceRepositoryAdapter();
    }
}
