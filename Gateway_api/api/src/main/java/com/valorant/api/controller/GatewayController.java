package com.valorant.api.controller;



import com.example.auth.grpc.*;
import com.exemplo.payment.grpc.PaymentGrpcServiceGrpc;
import com.exemplo.payment.grpc.PaymentRequest;
import com.exemplo.payment.grpc.PaymentResponse;
import com.exemplo.payment.grpc.WebhookRequest;
import com.valorant.api.DTO.SkinChromaDTO;
import com.valorant.api.DTO.SkinResponseDTO;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import valorant.SkinChromaRequest;

import valorant.ValorantServiceGrpc;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/validate")
public class GatewayController  {

    @GrpcClient("auth-service") // Bate com properties: grpc.client.auth-service...
    private AuthServiceGrpc.AuthServiceBlockingStub authService;

    @GrpcClient("valorant-service") // Bate com properties: grpc.client.valorant-service...
    private ValorantServiceGrpc.ValorantServiceBlockingStub valorantService;

    @GrpcClient("payment-service") // Bate com properties: grpc.client.payment-service...
    private PaymentGrpcServiceGrpc.PaymentGrpcServiceBlockingStub paymentStub;



    @GetMapping("/getSkin")
    public ResponseEntity<?> getSkins(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            var authResp = authService.validateToken(
                    ValidateTokenRequest.newBuilder()
                            .setToken(token)
                            .build()
            );

            if (!authResp.getIsValid()) {
                return ResponseEntity.status(401).body("Token inválido");
            }

            var skinResp = valorantService.getSkinsChromas(
                    SkinChromaRequest.newBuilder()
                            .setToken(token)
                            .build()
            );

            List<SkinChromaDTO> list = skinResp.getChromasList()
                    .stream()
                    .map(c -> new SkinChromaDTO(
                            c.getUuid(),
                            c.getDisplayName(),
                            c.getFullRender(),
                            c.getSwatch(),
                            c.getStreamedVideo()
                    ))
                    .toList();

            return ResponseEntity.ok(new SkinResponseDTO(list));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar skins: " + e.getMessage());

        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestBody GatewayPaymentDTO request) {
        try {
            log.info("Iniciando pagamento... com userId = {}, totalamoutn = {}", request.userId(), request.totalAmount());
            PaymentRequest grpcRequest = PaymentRequest.newBuilder()
                    .setUserId(request.userId())
                    .setTotalAmount(request.totalAmount())
                    .setEmail(request.payer().email())
                    .setTitle(request.items().getFirst().title())
                    .build();


            PaymentResponse response = paymentStub.createPayment(grpcRequest);


            return ResponseEntity.ok(new GatewayResponseDTO(response.getPaymentUrl()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    @PutMapping("/premium/{id}")
    public ResponseEntity<String> ativarPremium(@PathVariable Long id) {
        try {


            ActivatePremiumRequest request = ActivatePremiumRequest.newBuilder()
                    .setUserId(id)
                    .build();

            ActivatePremiumResponse response = authService.activatePremium(request);


            if (response.getSuccess()) {
                return ResponseEntity.ok(response.getMessage());
            } else {
                return ResponseEntity.badRequest().body(response.getMessage());
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro de comunicação gRPC");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestParam(value = "data.id", required = false) String id,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "topic", required = false) String topic) {

        String finalId = (id != null) ? id : "";
        String finalType = (type != null) ? type : topic;

        if (finalId.isEmpty()) {
            return ResponseEntity.ok("Ignorado (sem ID)");
        }

        try {

            WebhookRequest grpcRequest = WebhookRequest.newBuilder()
                    .setId(finalId)
                    .setType(finalType != null ? finalType : "payment")
                    .build();


            paymentStub.processWebhook(grpcRequest);

            return ResponseEntity.ok("Recebido");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no Gateway: " + e.getMessage());
        }
    }
}

record GatewayPaymentDTO(Long userId, Double totalAmount, PayerDTO payer, java.util.List<ItemDTO> items) {}
record PayerDTO(String email) {}
record ItemDTO(String title) {}
record GatewayResponseDTO(String redirectUrl) {}
