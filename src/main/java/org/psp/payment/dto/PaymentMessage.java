package org.psp.payment.dto;/* admin created on 4/2/2025  */

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PaymentMessage {
    private String message;
    private String channel;

    public PaymentMessage(GatewayConnectorResponse connectorResponse) {
        this.message = connectorResponse.getMessage();
        this.channel = connectorResponse.getPaymentNetwork();
    }

    public PaymentMessage(PackedMessageResponse packedMessageResponse){
        this.message = packedMessageResponse.getMessage();
        this.channel = packedMessageResponse.getDestination();
    }
}
