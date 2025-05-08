package org.psp.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UnpackedMessageResponse {
    private String channel;
    private String merchantId;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private String encryptedCardNumber;
    private String traceNumber;
    private boolean isRequest;
    private String responseCode;

    private boolean error;
    private String errorMessage;

    public boolean hasError() {
        return error;
    }
}
