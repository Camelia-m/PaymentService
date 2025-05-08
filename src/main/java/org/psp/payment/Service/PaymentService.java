package org.psp.payment.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.psp.payment.client.AuthenticationServiceClient;
import org.psp.payment.client.GatewayConnectorClient;
import org.psp.payment.client.MessageServiceClient;
import org.psp.payment.client.RouterServiceClient;
import org.psp.payment.dto.*;
import org.psp.payment.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TransactionService transactionService;
    private final MessageServiceClient messageServiceClient;
    private final AuthenticationServiceClient authenticationServiceClient;
    private final RouterServiceClient routerServiceClient;
    private final GatewayConnectorClient gatewayConnectorClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentMessage processTransaction(PaymentMessage message)  {
        Transaction transaction = new Transaction();
        try{
            UnpackedMessageResponse unpackMessage = messageServiceClient.unpackMessage(message);
            transactionService.updateFromTransformedMessage(transaction, unpackMessage);
            if (unpackMessage.hasError()) {
                return handleErrorResponse("Message transform failed", transaction, message.getChannel());
            }

            AuthenticationResponse authResponse = authenticationServiceClient.validateTransaction(new AuthenticationRequest(transaction));
            transactionService.updateAuthenticationInfo(transaction, authResponse);
            if (authResponse.hasError()) {
                return handleErrorResponse("Authentication failed", transaction, message.getChannel());
            }

            RouterResponse routerResponse = routerServiceClient.findDestination(new RouterRequest(transaction));
            transaction.updateRoutingInfo(transaction, routerResponse);
            if (routerResponse.hasError()){
                return handleErrorResponse("Finding paymentNetwork failed", transaction, message.getChannel());
            }

            kafkaTemplate.send("search-events", objectMapper.writeValueAsString(transaction));
            transactionService.save(transaction);
            return sendToPaymentNetwork(transaction);

        }catch (Exception exception){
            return handleErrorResponse("SYSTEM_ERROR", transaction, transaction.getChannel());
        }


    }

    public PaymentMessage sendToPaymentNetwork(Transaction transaction){
        try {
            PackedMessageResponse packedMessageResponse = messageServiceClient.packMessage(new PackedMessageRequest(transaction));
            if (packedMessageResponse.hasError()) {
                return handleErrorResponse("Transform for destination failed", transaction, transaction.getChannel());
            }
            GatewayConnectorResponse connectorResponse = gatewayConnectorClient.sendTransaction(new GatewayConnectorRequest(packedMessageResponse));
            if(connectorResponse.hasError()){
                return handleErrorResponse("Gateway connector failed", transaction, transaction.getChannel());
            }
            return handlePaymentNetworkResponse(connectorResponse);

        } catch (FeignException e) {
            return handleErrorResponse("NETWORK_ERROR", transaction, transaction.getChannel());
        } catch (Exception e) {
            return handleErrorResponse("SYSTEM_ERROR", transaction, transaction.getChannel());
        }
    }

    private PaymentMessage handlePaymentNetworkResponse(GatewayConnectorResponse connectorResponse) throws JsonProcessingException {
        UnpackedMessageResponse unpackMessage = messageServiceClient.unpackMessage(new PaymentMessage(connectorResponse));
        Optional<Transaction> transactionOptional = transactionService.findTransactionByDateAndTraceNumber(unpackMessage.getTraceNumber(), unpackMessage.getDateTime());
        if (transactionOptional.isEmpty()){
            Transaction transaction = new Transaction();
            transactionService.updateFromTransformedMessage(transaction, unpackMessage);
            return handleErrorResponse("Request transaction not found", transaction, transaction.getChannel());
        }
        Transaction transaction = transactionOptional.get();
        transactionService.updateFromTransformedMessage(transaction, unpackMessage);

        kafkaTemplate.send("search-events", objectMapper.writeValueAsString(transaction));
        transactionService.save(transaction);

        PackedMessageResponse packedMessageResponse = messageServiceClient.packMessage(new PackedMessageRequest(transaction));
        return new PaymentMessage(packedMessageResponse);
    }

    PaymentMessage handleErrorResponse(String message, Transaction transaction, String destination) {
        transaction.setResponseCode("99");
        transaction.setResponseErrorMessage(message);

        if (transaction.getChannel() == null || transaction.getChannel().isEmpty())
            transaction.setChannel(destination);

        try {
            kafkaTemplate.send("search-events", objectMapper.writeValueAsString(transaction));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        transactionService.save(transaction);
        PackedMessageResponse messageResponse = messageServiceClient.packMessage(new PackedMessageRequest(transaction));
        return new PaymentMessage(messageResponse);
    }
}
