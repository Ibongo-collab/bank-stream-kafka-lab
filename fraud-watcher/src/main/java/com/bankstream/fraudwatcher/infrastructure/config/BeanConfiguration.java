package com.bankstream.fraudwatcher.infrastructure.config;


import com.bankstream.fraudwatcher.application.port.out.FraudAlertRepositoryPort;
import com.bankstream.fraudwatcher.application.port.out.TransactionHistoryPort;
import com.bankstream.fraudwatcher.infrastructure.adapter.out.persistence.InMemoryFraudAlertRepositoryAdapter;
import com.bankstream.fraudwatcher.infrastructure.adapter.out.persistence.InMemoryTransactionHistoryAdapter;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public FraudAlertRepositoryPort fraudAlertRepositoryPort() {
        return new InMemoryFraudAlertRepositoryAdapter();
    }

    @Bean
    public TransactionHistoryPort transactionHistoryPort() {
        return new InMemoryTransactionHistoryAdapter();
    }

    @Bean
    public Duration velocityWindow(FraudRulesProperties properties) {
        return Duration.ofSeconds(properties.velocityWindowSeconds());
    }

}
