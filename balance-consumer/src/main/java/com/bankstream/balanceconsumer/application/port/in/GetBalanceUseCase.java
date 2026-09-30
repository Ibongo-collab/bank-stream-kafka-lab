package com.bankstream.balanceconsumer.application.port.in;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;

public interface GetBalanceUseCase {

    Balance getBalance(AccountId accountId);
}
