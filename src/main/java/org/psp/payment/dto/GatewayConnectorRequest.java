package org.psp.payment.dto;

import lombok.Data;

@Data
public class GatewayConnectorRequest {
    private String message;
    private String paymentNetwork;
    public GatewayConnectorRequest(PackedMessageResponse packedMessageResponse) {
        this.message = packedMessageResponse.getMessage();
        this.paymentNetwork = packedMessageResponse.getDestination();
    }
}
