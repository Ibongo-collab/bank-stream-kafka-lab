package com.bankstream.fraudwatcher.infrastructure.config;

import com.bankstream.fraudwatcher.domain.rules.FraudRule;
import com.bankstream.fraudwatcher.domain.rules.HighAmountRule;
import com.bankstream.fraudwatcher.domain.rules.VelocityRule;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Assembles the active rule set from {@link FraudRulesProperties}. Adding
 * a new rule to production is: write the domain class implementing
 * {@link FraudRule}, add one line to the list below. No other file in the
 * application changes.
 */
@Configuration
@EnableConfigurationProperties(FraudRulesProperties.class)
public class FraudRulesConfiguration {

    @Bean
    public List<FraudRule> fraudRules(FraudRulesProperties properties) {
        return List.of(
                new HighAmountRule(properties.highAmountThreshold(), "XAF"),
                new VelocityRule(properties.velocityMaxCount())
        );
    }
}
