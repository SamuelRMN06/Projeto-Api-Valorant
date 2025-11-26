package com.example.ProjetoIntegracao.grpc;




import com.example.ProjetoIntegracao.auth.JwtUtil;
import com.example.ProjetoIntegracao.model.UsuarioModel;
import com.example.ProjetoIntegracao.service.UsuarioService;


import com.example.auth.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class AuthGrpcController extends AuthServiceGrpc.AuthServiceImplBase {


    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @Override
    public void validateToken(ValidateTokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        try {
            log.info("=== AUTHSERVICE DEBUG ===");
            log.info("Recebendo validação de token");

            String token = request.getToken();
            boolean isValid = jwtUtil.validateToken(token);

            ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                    .setIsValid(isValid)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());

            ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                    .setIsValid(false)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }


    @Override
    public void activatePremium(ActivatePremiumRequest request, StreamObserver<ActivatePremiumResponse> responseObserver) {
        log.info("Recebendo solicitação gRPC para ativar premium do ID: {}", request.getUserId());

        try {
            // A conversão de long (primitivo) para Long (objeto) acontece aqui
            Boolean resultado = usuarioService.SetPremium(Long.valueOf(request.getUserId()));

            ActivatePremiumResponse response = ActivatePremiumResponse.newBuilder()
                    .setSuccess(resultado)
                    .setMessage("Premium ativado com sucesso!")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Erro ao ativar premium: {}", e.getMessage());

            ActivatePremiumResponse response = ActivatePremiumResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Erro: " + e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        log.info("Recebendo solicitação gRPC para cadastro de: {}", request.getEmail());

        try {
            // 1. CONSTRÓI O OBJETO UsuarioModel AQUI (No Controller/Adaptador)
            UsuarioModel novoUsuario = new UsuarioModel();
            novoUsuario.setNome(request.getName());
            novoUsuario.setEmail(request.getEmail());
            novoUsuario.setSenha(request.getPassword()); // Senha bruta

            // 2. CHAMA O SERVIÇO PASSANDO O OBJETO COMPLETO
            usuarioService.registrar(novoUsuario);

            // 3. Monta resposta de sucesso
            RegisterResponse response = RegisterResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Usuário criado com sucesso e pronto para pagar!")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Erro no cadastro gRPC: {}", e.getMessage());

            RegisterResponse response = RegisterResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Falha ao cadastrar: " + e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            // 1. Autentica o usuário no serviço de negócio
            var usuario = usuarioService.autenticar(request.getEmail(), request.getPassword());

            if (usuario != null) {
                // 2. Gera o Token e monta a resposta de sucesso
                String token = jwtUtil.generateToken(usuario.getEmail());

                LoginResponse response = LoginResponse.newBuilder()
                        .setToken(token)
                        .setPremium(usuario.isPremium())
                        .setId(usuario.getId())
                        .build();

                responseObserver.onNext(response);
            } else {
                // 3. Login falhou (Credenciais inválidas)
                responseObserver.onNext(
                        LoginResponse.newBuilder()
                                .setError("Credenciais inválidas")
                                .build()
                );
            }
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Erro no processamento de Login: {}", e.getMessage());
            // Em caso de erro interno, o gRPC informa o cliente
            responseObserver.onError(e);
        }
    }
}