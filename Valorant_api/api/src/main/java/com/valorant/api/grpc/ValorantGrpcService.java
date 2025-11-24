package com.valorant.api.grpc;

import auth.AuthServiceGrpc;
import com.valorant.api.dtos.SkinDTO;
import com.valorant.api.service.ApiService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import valorant.Valorant;
import valorant.ValorantServiceGrpc;
import auth.Auth;
import auth.AuthServiceGrpc;
import java.util.List;

@GrpcService
public class ValorantGrpcService extends ValorantServiceGrpc.ValorantServiceImplBase {

    @GrpcClient("authService")
    private AuthServiceGrpc.AuthServiceBlockingStub authClient;

    ApiService apiService = new ApiService();

    public ValorantGrpcService(ApiService apiService) {
        this.apiService = apiService;
    }



    @Override
    public void getSkinsChromas(Valorant.SkinChromaRequest request, StreamObserver<Valorant.SkinChromaResponse> responseObserver) {
        try {

            Auth.ValidateTokenResponse authResp = authClient.validateToken(
                    Auth.ValidateTokenRequest.newBuilder()
                            .setToken(request.getToken())
                            .build()
            );

            if (!authResp.getIsValid()) {
                responseObserver.onError(
                        Status.UNAUTHENTICATED
                                .withDescription("Token inválido")
                                .asRuntimeException()
                );
                return;
            }

            if (request.getToken().isEmpty()) {
                responseObserver.onError(
                        Status.UNAUTHENTICATED
                                .withDescription("Token inválido")
                                .asRuntimeException()
                );
                return;
            }

            SkinDTO response = apiService.getSkin();

            if (response == null || response.data == null) {
                responseObserver.onError(
                        Status.INTERNAL
                                .withDescription("API Valorant retornou vazio")
                                .asRuntimeException()
                );
                return;
            }
            List<Valorant.SkinChroma> chromas = response.data.stream()
                    .map(item ->
                            Valorant.SkinChroma.newBuilder()
                                    .setUuid(item.getUuid() != null ? item.getUuid() : "")
                                    .setDisplayName(item.getDisplayName() != null ? item.getDisplayName() : "")
                                    .setFullRender(item.getFullRender() != null ? item.getFullRender() : "")
                                    .setSwatch(item.getSwatch() != null ? item.getSwatch() : "")
                                    .setStreamedVideo(item.getStreamedVideo() != null ? item.getStreamedVideo() : "")
                                    .build()
                    ).toList();

            Valorant.SkinChromaResponse grpcResponse = Valorant.SkinChromaResponse.newBuilder()
                    .addAllChromas(chromas)
                    .build();

            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Erro no serviço Valorant: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
