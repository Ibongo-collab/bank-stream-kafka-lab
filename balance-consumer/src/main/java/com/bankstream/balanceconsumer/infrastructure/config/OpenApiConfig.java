package com.bankstream.balanceconsumer.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI balanceConsumerOpenApi() {
        return new OpenAPI().info(new Info()
                .title("balance-consumer API")
                .description("Maintains account balances from the BankStream Kafka lab pipeline")
                .version("v1"));
    }
}
