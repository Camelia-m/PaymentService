package org.psp.payment.client;/* admin created on 4/2/2025  */

import org.psp.payment.dto.AuthenticationRequest;
import org.psp.payment.dto.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authentication-service", url = "http://authentication-service:8083")
public interface AuthenticationServiceClient {

    @PostMapping("/auth/validation")
    AuthenticationResponse validateTransaction(@RequestBody AuthenticationRequest authenticationRequest);
}
