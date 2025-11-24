package com.valorant.api.config;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import auth.AuthServiceGrpc;
import valorant.ValorantServiceGrpc;

@Configuration
public class GrpcClientConfig {

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        return AuthServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public ValorantServiceGrpc.ValorantServiceBlockingStub valorantServiceStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9091)
                .usePlaintext()
                .build();

        return ValorantServiceGrpc.newBlockingStub(channel);
    }
}
