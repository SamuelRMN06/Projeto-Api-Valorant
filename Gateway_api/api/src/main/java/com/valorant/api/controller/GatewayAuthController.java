package com.valorant.api.controller;


import com.example.auth.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class GatewayAuthController {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    // --- LOGIN (JÁ EXISTIA) ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            LoginRequest grpcRequest = LoginRequest.newBuilder()
                    .setEmail(request.email())
                    .setPassword(request.senha())
                    .build();

            LoginResponse response = authStub.login(grpcRequest);

            if (!response.getError().isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("erro", response.getError()));
            }

            return ResponseEntity.ok(Map.of(
                    "token", response.getToken(),
                    "premium", response.getPremium(),
                    "id", response.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no Gateway (Login): " + e.getMessage());
        }
    }

    // --- REGISTRAR (ADICIONE ISTO AGORA!) ---
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegisterDTO request) {
        try {
            // 1. Converte JSON do Frontend para gRPC
            RegisterRequest grpcRequest = RegisterRequest.newBuilder()
                    .setName(request.nome())   // Atenção: o front manda "nome"
                    .setEmail(request.email())
                    .setPassword(request.senha()) // Atenção: o front manda "senha"
                    .build();

            // 2. Chama o AuthService
            RegisterResponse response = authStub.register(grpcRequest);

            // 3. Responde ao Frontend
            if (response.getSuccess()) {
                return ResponseEntity.ok(Map.of("message", "Usuário criado com sucesso!"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", response.getMessage()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no Gateway (Registro): " + e.getMessage());
        }
    }
}

// --- DTOs ---
record LoginDTO(String email, String senha) {}

// ADICIONE ESTE DTO TAMBÉM:
record RegisterDTO(String nome, String email, String senha) {}