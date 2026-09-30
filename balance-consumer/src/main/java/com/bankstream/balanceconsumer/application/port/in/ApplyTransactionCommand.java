package com.bankstream.balanceconsumer.application.port.in;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;


public record ApplyTransactionCommand(
        UUID transactionId,
        AccountId accountId,
        TransactionType type,
        BigDecimal amount,
        String currencyCode
) {
}
