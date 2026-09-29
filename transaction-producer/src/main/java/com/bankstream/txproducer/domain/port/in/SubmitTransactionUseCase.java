package com.bankstream.txproducer.domain.port.in;

import com.bankstream.txproducer.domain.model.Transaction;


public interface SubmitTransactionUseCase {

    Transaction submit(SubmitTransactionCommand command);
}
