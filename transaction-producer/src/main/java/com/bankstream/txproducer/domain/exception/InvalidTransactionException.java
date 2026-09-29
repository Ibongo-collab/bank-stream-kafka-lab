package com.bankstream.txproducer.domain.exception;

/** Raised when a transaction would violate a domain invariant (e.g. a non-positive amount). */
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }
}
