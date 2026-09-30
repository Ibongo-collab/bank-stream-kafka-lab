package com.bankstream.fraudwatcher.infrastructure.config;

import com.bankstream.fraudwatcher.application.FraudDetectionService;
import com.bankstream.fraudwatcher.application.port.out.FraudAlertRepositoryPort;
import com.bankstream.fraudwatcher.application.port.out.TransactionHistoryPort;
import com.bankstream.fraudwatcher.domain.rules.FraudRule;
import com.bankstream.fraudwatcher.infrastructure.adapter.out.persistence.InMemoryFraudAlertRepositoryAdapter;
import com.bankstream.fraudwatcher.infrastructure.adapter.out.persistence.InMemoryTransactionHistoryAdapter;
import java.time.Duration;
import java.util.List;
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
    public FraudDetectionService fraudDetectionService(
            List<FraudRule> fraudRules,
            FraudAlertRepositoryPort fraudAlertRepositoryPort,
            TransactionHistoryPort transactionHistoryPort,
            FraudRulesProperties properties) {
        return new FraudDetectionService(
                fraudRules,
                fraudAlertRepositoryPort,
                transactionHistoryPort,
                Duration.ofSeconds(properties.velocityWindowSeconds()));
    }

    // TODO(kafka-lab): once wired, add a KafkaTransactionListenerAdapter
    // bean here — its OWN consumer group, separate from balance-consumer's
    // — that consumes "transactions.completed" and calls
    // FraudDetectionService#evaluate for every event.
}
