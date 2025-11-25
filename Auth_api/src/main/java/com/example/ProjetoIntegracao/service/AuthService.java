package com.example.ProjetoIntegracao.service;

import auth.Auth;
import auth.AuthServiceGrpc;
import com.example.ProjetoIntegracao.auth.JwtUtil;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void validateToken(Auth.ValidateTokenRequest request, StreamObserver<Auth.ValidateTokenResponse> responseObserver) {
        try{

            System.out.println("=== AUTHSERVICE DEBUG ===");
            System.out.println("TOKEN RECEBIDO: [" + request.getToken() + "]");
            String token = request.getToken();
            boolean isValid = jwtUtil.validateToken(token);

            Auth.ValidateTokenResponse validateTokenResponse = Auth.ValidateTokenResponse.newBuilder()
                    .setIsValid(isValid)
                    .build();

            responseObserver.onNext(validateTokenResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            Auth.ValidateTokenResponse validateTokenResponse = Auth.ValidateTokenResponse.newBuilder()
                    .setIsValid(false)
                    .build();
            responseObserver.onNext(validateTokenResponse);
            responseObserver.onCompleted();
        }

    }
}
