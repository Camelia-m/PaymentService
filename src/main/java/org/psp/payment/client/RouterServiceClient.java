package org.psp.payment.client;

import org.psp.payment.dto.RouterRequest;
import org.psp.payment.dto.RouterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "router-service", url = "http://router-service:8084")
public interface RouterServiceClient {

    @PostMapping("/route/destination")
    RouterResponse findDestination(@RequestBody RouterRequest request);
}
