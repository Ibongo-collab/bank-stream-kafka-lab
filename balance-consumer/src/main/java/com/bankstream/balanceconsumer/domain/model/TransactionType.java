package com.bankstream.balanceconsumer.domain.model;

/**
 * Deliberately duplicated from transaction-producer's own enum rather
 * than shared through a common library. Each service owns its view of the
 * event contract (consumer-driven contract style) — coupling them through
 * a shared JAR would mean a change to one service's model forcing a
 * release of every other service that depends on the library.
 */
public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL
}
