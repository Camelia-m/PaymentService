package org.psp.payment.dto;

import lombok.Data;
import org.psp.payment.model.Transaction;

import java.math.BigDecimal;

@Data
public class AuthenticationRequest {
    private String merchantId;
    private String transactionType;
    private BigDecimal amount;
    private String encryptedCardNumber;

    public AuthenticationRequest(Transaction transaction){
        this.merchantId = transaction.getMerchantId();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.encryptedCardNumber = transaction.getEncryptedCardNumber();
    }
}
