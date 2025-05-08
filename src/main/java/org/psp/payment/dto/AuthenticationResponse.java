package org.psp.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private boolean error;
    private String errorMessage;

    public boolean hasError() {
        return error;
    }
}

