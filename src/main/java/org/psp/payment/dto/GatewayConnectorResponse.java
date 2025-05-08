package org.psp.payment.dto;

import lombok.Data;

@Data
public class GatewayConnectorResponse {
    private String message;
    private String paymentNetwork;
    private boolean error;
    private String errorMessage;

    public boolean hasError() {
        return error;
    }
}
