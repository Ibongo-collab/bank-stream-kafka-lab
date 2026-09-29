package com.bankstream.fraudwatcher.infrastructure.config;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds the {@code fraud.rules.*} keys from application.yml. This class
 * lives in infrastructure, not domain — the domain rule classes
 * ({@code HighAmountRule}, {@code VelocityRule}) take plain constructor
 * arguments and have never heard of Spring's config-binding machinery.
 */
@ConfigurationProperties(prefix = "fraud.rules")
public record FraudRulesProperties(
        BigDecimal highAmountThreshold,
        int velocityMaxCount,
        long velocityWindowSeconds
) {
}
