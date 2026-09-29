package com.bankstream.txproducer.infrastructure.adapter.in.web.dto;

import com.bankstream.txproducer.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        TransactionType type,
        BigDecimal amount,
        String currencyCode,
        Instant occurredAt
) {
}
