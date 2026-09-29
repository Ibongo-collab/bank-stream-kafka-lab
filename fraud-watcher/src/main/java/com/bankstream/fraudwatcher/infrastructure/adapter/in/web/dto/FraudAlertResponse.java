package com.bankstream.fraudwatcher.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

public record FraudAlertResponse(
        UUID id,
        UUID transactionId,
        UUID accountId,
        String reason,
        String severity,
        Instant detectedAt
) {
}
