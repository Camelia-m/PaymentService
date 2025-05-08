package org.psp.payment.controller;

import lombok.RequiredArgsConstructor;
import org.psp.payment.Service.PaymentService;
import org.psp.payment.dto.PaymentMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentMessage> processTransaction(@RequestBody PaymentMessage request) {
        return ResponseEntity.ok(paymentService.processTransaction(request));
    }
}
