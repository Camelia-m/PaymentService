package org.psp.payment.repository;/* admin created on 4/2/2025  */

import org.psp.payment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByTraceNumber(String traceNumber);

    Optional<Transaction> findByTraceNumberAndDateTime(String traceNumber, LocalDateTime dateTime);
}
