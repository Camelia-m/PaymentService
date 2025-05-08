package org.psp.payment.dto;

import lombok.Data;
import org.psp.payment.model.Transaction;

@Data
public class RouterRequest {
    private String channel;
    private String transactionType;

    public RouterRequest(Transaction transaction) {
        this.channel = transaction.getChannel();
        this.transactionType = transaction.getTransactionType();
    }
}
