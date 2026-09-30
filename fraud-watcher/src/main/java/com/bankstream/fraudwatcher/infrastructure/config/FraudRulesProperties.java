package com.bankstream.fraudwatcher.infrastructure.config;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "fraud.rules")
public record FraudRulesProperties(
        BigDecimal highAmountThreshold,
        int velocityMaxCount,
        long velocityWindowSeconds
) {
}
