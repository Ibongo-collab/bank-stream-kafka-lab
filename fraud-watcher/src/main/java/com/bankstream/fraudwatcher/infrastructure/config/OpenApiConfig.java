package com.bankstream.fraudwatcher.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fraudWatcherOpenApi() {
        return new OpenAPI().info(new Info()
                .title("fraud-watcher API")
                .description("Evaluates transactions against fraud rules for the BankStream Kafka lab")
                .version("v1"));
    }
}
