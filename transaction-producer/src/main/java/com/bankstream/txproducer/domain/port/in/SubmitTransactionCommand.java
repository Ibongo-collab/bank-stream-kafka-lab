package com.bankstream.txproducer.domain.port.in;

import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.TransactionType;
import java.math.BigDecimal;

/**
 * Input model for the {@link SubmitTransactionUseCase}. Deliberately
 * separate from both the web DTO (which knows about JSON/validation
 * annotations) and the domain {@code Transaction} (which is the result,
 * not the request) — this is the use case's own vocabulary.
 */
public record SubmitTransactionCommand(
        AccountId accountId,
        TransactionType type,
        BigDecimal amount,
        String currencyCode
) {
}
