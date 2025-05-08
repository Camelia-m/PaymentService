package org.psp.payment.Service;

import org.junit.jupiter.api.Test;
import org.psp.payment.client.AuthenticationServiceClient;
import org.psp.payment.client.GatewayConnectorClient;
import org.psp.payment.client.MessageServiceClient;
import org.psp.payment.client.RouterServiceClient;
import org.psp.payment.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableFeignClients
class PaymentServiceIntegrationTest {
    @Autowired
    private PaymentService paymentService;

    @MockBean
    private MessageServiceClient messageServiceClient;
    @MockBean
    private AuthenticationServiceClient authenticationServiceClient;
    @MockBean
    private RouterServiceClient routerServiceClient;
    @MockBean
    private GatewayConnectorClient gatewayConnectorClient;
    @Test
    void testProcessTransaction_integration() {
        // Set up mock data for the test
        PaymentMessage message = new PaymentMessage("POS", "request-message");
        UnpackedMessageResponse unpackedMessageResponse = new UnpackedMessageResponse();
        AuthenticationResponse authResponse = new AuthenticationResponse(false, null);
        RouterResponse routerResponse = new RouterResponse();
        GatewayConnectorResponse connectorResponse = new GatewayConnectorResponse();

        PackedMessageResponse packedMessageResponse = new PackedMessageResponse();
        packedMessageResponse.setMessage("response-message");
        packedMessageResponse.setDestination("POS");

        // Mock external service responses
        when(messageServiceClient.unpackMessage(any(PaymentMessage.class))).thenReturn(unpackedMessageResponse);
        when(authenticationServiceClient.validateTransaction(any(AuthenticationRequest.class))).thenReturn(authResponse);
        when(routerServiceClient.findDestination(any(RouterRequest.class))).thenReturn(routerResponse);
        when(gatewayConnectorClient.sendTransaction(any(GatewayConnectorRequest.class))).thenReturn(connectorResponse);
        when(messageServiceClient.packMessage(any(PackedMessageRequest.class))).thenReturn(packedMessageResponse);

        PaymentMessage result = paymentService.processTransaction(message);

        assertNotNull(result);

    }
}
