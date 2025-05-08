package org.psp.payment.dto;

import lombok.Data;
import org.psp.payment.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PackedMessageRequest {
    private String channel;
    private String merchantId;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private String encryptedCardNumber;
    private String traceNumber;
    private boolean isRequest;

    public PackedMessageRequest(Transaction transaction) {
        this.channel = transaction.getChannel();
        this.merchantId = transaction.getMerchantId();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.dateTime = transaction.getDateTime();
        this.encryptedCardNumber = transaction.getEncryptedCardNumber();
        this.traceNumber = transaction.getTraceNumber();
        this.isRequest = transaction.isRequest();
    }
}
