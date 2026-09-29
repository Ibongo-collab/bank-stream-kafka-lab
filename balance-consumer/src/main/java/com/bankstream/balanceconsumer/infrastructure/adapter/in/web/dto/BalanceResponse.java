package com.bankstream.balanceconsumer.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BalanceResponse(
        UUID accountId,
        BigDecimal amount,
        String currencyCode,
        Instant lastUpdatedAt
) {
}
