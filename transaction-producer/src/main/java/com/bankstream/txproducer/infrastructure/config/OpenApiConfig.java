package com.bankstream.txproducer.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transactionProducerOpenApi() {
        return new OpenAPI().info(new Info()
                .title("transaction-producer API")
                .description("Submits bank transactions into the BankStream Kafka lab pipeline")
                .version("v1"));
    }
}
