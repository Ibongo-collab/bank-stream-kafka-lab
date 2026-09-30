package com.bankstream.txproducer.application.port.in;

import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.TransactionType;
import java.math.BigDecimal;


public record SubmitTransactionCommand(
        AccountId accountId,
        TransactionType type,
        BigDecimal amount,
        String currencyCode
) {
}
