package org.psp.payment.Service;


import lombok.RequiredArgsConstructor;
import org.psp.payment.dto.AuthenticationResponse;
import org.psp.payment.dto.UnpackedMessageResponse;
import org.psp.payment.model.Transaction;
import org.psp.payment.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public void updateFromTransformedMessage(Transaction transaction, UnpackedMessageResponse transformResponse) {
        transaction.setChannel(transformResponse.getChannel());
        transaction.setMerchantId(transformResponse.getMerchantId());
        transaction.setTransactionType(transformResponse.getTransactionType());
        transaction.setAmount(transformResponse.getAmount());
        transaction.setDateTime(transformResponse.getDateTime());
        transaction.setEncryptedCardNumber(transformResponse.getEncryptedCardNumber());
        transaction.setTraceNumber(transformResponse.getTraceNumber());
        if (transformResponse.getResponseCode() != null)
            transaction.setResponseCode(transformResponse.getResponseCode());
        if(transformResponse.hasError())
            transaction.setTransformError(transformResponse.getErrorMessage());
    }

    public void updateAuthenticationInfo(Transaction transaction, AuthenticationResponse authResponse) {
        if(Boolean.TRUE.equals(authResponse.hasError())){
            transaction.setAuthenticationError(authResponse.getErrorMessage());
        }
    }

    @Transactional
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Optional<Transaction> findTransactionByDateAndTraceNumber(String traceNumber, LocalDateTime dateTime) {
        return transactionRepository.findByTraceNumberAndDateTime(traceNumber, dateTime);
    }
}
