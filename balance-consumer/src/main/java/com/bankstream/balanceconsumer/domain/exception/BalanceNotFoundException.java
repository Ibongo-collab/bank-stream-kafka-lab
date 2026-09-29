package com.bankstream.balanceconsumer.domain.exception;

import com.bankstream.balanceconsumer.domain.model.AccountId;

public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException(AccountId accountId) {
        super("No balance found for account " + accountId);
    }
}
