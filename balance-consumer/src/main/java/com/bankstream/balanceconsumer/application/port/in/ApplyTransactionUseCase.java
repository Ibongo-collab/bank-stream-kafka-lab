package com.bankstream.balanceconsumer.application.port.in;

import com.bankstream.balanceconsumer.domain.model.Balance;


public interface ApplyTransactionUseCase {

    Balance apply(ApplyTransactionCommand command);
}
