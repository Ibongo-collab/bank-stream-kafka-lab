package com.bankstream.balanceconsumer.domain.exception;

import com.bankstream.balanceconsumer.domain.model.AccountId;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(AccountId accountId) {
        super("Insufficient funds on account " + accountId + " for this withdrawal");
    }
}
