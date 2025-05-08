package org.psp.payment.dto;

import lombok.Data;

@Data
public class RouterResponse {
    private boolean error;
    private String errorMessage;
    private String paymentNetwork;

    public boolean hasError() {
        return error;
    }
}
