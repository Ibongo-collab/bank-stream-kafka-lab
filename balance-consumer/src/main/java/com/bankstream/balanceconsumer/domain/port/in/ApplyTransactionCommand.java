package com.bankstream.balanceconsumer.domain.port.in;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * What it takes to update a balance. Note {@code transactionId}: it's not
 * used by the domain logic today, but it's exactly the field the future
 * Kafka listener will need for idempotency (skip if this transaction id
 * was already applied) — kept here now so that plumbing it through later
 * doesn't mean touching every layer again.
 */
public record ApplyTransactionCommand(
        UUID transactionId,
        AccountId accountId,
        TransactionType type,
        BigDecimal amount,
        String currencyCode
) {
}
