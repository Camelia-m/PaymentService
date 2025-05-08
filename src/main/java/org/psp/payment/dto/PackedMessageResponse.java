package org.psp.payment.dto;

import lombok.Data;

@Data
public class PackedMessageResponse {
    private String message;
    private String destination;
    private boolean error;
    private String errorMessage;

    public boolean hasError() {
        return error;
    }
}
