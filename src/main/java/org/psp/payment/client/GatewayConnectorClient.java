package org.psp.payment.client;

import org.psp.payment.dto.GatewayConnectorRequest;
import org.psp.payment.dto.GatewayConnectorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gateway-connector-service", url = "http://gateway-connector-service:8085")
public interface GatewayConnectorClient {
    @PostMapping("/connector/transaction")
    GatewayConnectorResponse sendTransaction(@RequestBody GatewayConnectorRequest request);
}
