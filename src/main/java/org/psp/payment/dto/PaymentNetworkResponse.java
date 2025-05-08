package org.psp.payment.dto;

import lombok.Data;

@Data
public class PaymentNetworkResponse {
    private String responseCode;
    private String message;
    private String traceNumber;
}
