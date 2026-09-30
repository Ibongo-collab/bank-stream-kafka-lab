package com.bankstream.balanceconsumer.domain.port.in;

import com.bankstream.balanceconsumer.domain.model.Balance;


public interface ApplyTransactionUseCase {

    Balance apply(ApplyTransactionCommand command);
}
