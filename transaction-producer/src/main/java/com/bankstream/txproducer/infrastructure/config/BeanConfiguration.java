package com.bankstream.txproducer.infrastructure.config;

import com.bankstream.txproducer.application.TransactionService;
import com.bankstream.txproducer.application.port.out.TransactionPublisherPort;
import com.bankstream.txproducer.application.port.out.TransactionRepositoryPort;
import com.bankstream.txproducer.infrastructure.adapter.out.persistence.InMemoryTransactionRepositoryAdapter;
import com.bankstream.txproducer.infrastructure.adapter.out.publisher.KafkaTransactionPublisherAdapter;
import com.bankstream.txproducer.infrastructure.adapter.out.publisher.TransactionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
public class BeanConfiguration {

    @Bean
    public TransactionRepositoryPort transactionRepositoryPort() {
        return new InMemoryTransactionRepositoryAdapter();
    }

    @Bean
    public KafkaTemplate<String, TransactionEvent> transactionkafkaTemplate(ProducerFactory<String, TransactionEvent> transactionProducerFactory) {
        return new KafkaTemplate<>(transactionProducerFactory);
    }

    @Bean
    public TransactionPublisherPort transactionPublisherPort(
            KafkaTemplate<String, TransactionEvent> transactionKafkaTemplate,
            @Value("${app.kafka.topic.transactions}") String transactionsTopic) {
        return new KafkaTransactionPublisherAdapter(transactionKafkaTemplate, transactionsTopic);
    }

    @Bean
    public TransactionService transactionService(
            TransactionRepositoryPort transactionRepositoryPort,
            TransactionPublisherPort transactionPublisherPort) {
        return new TransactionService(transactionRepositoryPort, transactionPublisherPort);
    }
}
