package com.bankstream.txproducer.domain.port.in;

import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.Transaction;
import java.util.List;


public interface ListTransactionsUseCase {

    List<Transaction> listAll();

    List<Transaction> listByAccount(AccountId accountId);
}
