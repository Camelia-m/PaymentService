package org.psp.payment.client;

import org.psp.payment.dto.PackedMessageRequest;
import org.psp.payment.dto.PackedMessageResponse;
import org.psp.payment.dto.UnpackedMessageResponse;
import org.psp.payment.dto.PaymentMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "message-service", url = "http://message-service:8082")
public interface MessageServiceClient {

    @PostMapping("/message/unpack")
    UnpackedMessageResponse unpackMessage(@RequestBody PaymentMessage paymentMessage);

    @PostMapping("/message/pack")
    PackedMessageResponse packMessage(@RequestBody PackedMessageRequest packedMessageRequest);

}
