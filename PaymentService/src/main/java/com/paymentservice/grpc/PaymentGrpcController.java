package com.paymentservice.grpc;

import com.exemplo.payment.grpc.*;
import com.paymentservice.dto.CreatePreferenceRequestDTO;
import com.paymentservice.dto.CreateResponseDTO;
import com.paymentservice.service.CreatePaymentPreferenceService;
import com.paymentservice.service.ProccessPaymentNotificationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class PaymentGrpcController extends PaymentGrpcServiceGrpc.PaymentGrpcServiceImplBase {


    private final CreatePaymentPreferenceService createPaymentService;
    @Autowired
    private ProccessPaymentNotificationService notificationService;

    @Override
    public void createPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        log.info("Recebendo requisição gRPC para user: {}", request.getUserId());

        try {

            CreatePreferenceRequestDTO.ItemDTO item = new CreatePreferenceRequestDTO.ItemDTO(
                    "1",
                    request.getTitle(),
                    1,
                    BigDecimal.valueOf(request.getTotalAmount())
            );


            CreatePreferenceRequestDTO.PayerDTO payer = new CreatePreferenceRequestDTO.PayerDTO(
                    "Usuario " + request.getUserId(),
                    request.getEmail()
            );


            CreatePreferenceRequestDTO.BackUrlsDTO backUrls = new CreatePreferenceRequestDTO.BackUrlsDTO(
                    "http://localhost:5173/checkout/success",
                    "http://localhost:5173failure",
                    "http://localhost:5173/checkout/pending"
            );


            CreatePreferenceRequestDTO inputData = new CreatePreferenceRequestDTO(
                    request.getUserId(),
                    BigDecimal.valueOf(request.getTotalAmount()),
                    payer,
                    backUrls,
                    null,
                    "approved",
                    List.of(item)
            );


            CreateResponseDTO serviceResponse = createPaymentService.createPrefernce(inputData);


            PaymentResponse grpcResponse = PaymentResponse.newBuilder()
                    .setPaymentUrl(serviceResponse.redirectUrl())
                    .build();


            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Erro no processamento gRPC: {}", e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void processWebhook(WebhookRequest request, StreamObserver<WebhookResponse> responseObserver) {
        log.info("gRPC Webhook recebido: ID={} Type={}", request.getId(), request.getType());

        try {

            var resultado = notificationService.proccessPaymentNotification(request.getId(), request.getType());

            WebhookResponse response = WebhookResponse.newBuilder()
                    .setStatus(resultado.isSuccess() ? "OK" : "ERROR")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Erro no webhook gRPC: {}", e.getMessage());
            responseObserver.onError(e);
        }
    }
}