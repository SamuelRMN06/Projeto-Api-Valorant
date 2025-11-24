package com.valorant.api.controller;


import auth.Auth;
import auth.AuthServiceGrpc;
import com.valorant.api.DTO.SkinChromaDTO;
import com.valorant.api.DTO.SkinResponseDTO;
import com.valorant.api.DTO.TokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import valorant.Valorant;
import valorant.ValorantServiceGrpc;

import java.util.List;

@RestController
@RequestMapping("/validate")
public class GatewayController  {

    private final AuthServiceGrpc.AuthServiceBlockingStub authService;
    private final ValorantServiceGrpc.ValorantServiceBlockingStub valorantService;

    public GatewayController(
            AuthServiceGrpc.AuthServiceBlockingStub authService,
            ValorantServiceGrpc.ValorantServiceBlockingStub valorantService) {
        this.authService = authService;
        this.valorantService = valorantService;
    }

    @GetMapping("/getSkin")
    public ResponseEntity<?> getSkins(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            var authResp = authService.validateToken(
                    Auth.ValidateTokenRequest.newBuilder()
                            .setToken(token)
                            .build()
            );

            if (!authResp.getIsValid()) {
                return ResponseEntity.status(401).body("Token inválido");
            }

            var skinResp = valorantService.getSkinsChromas(
                    Valorant.SkinChromaRequest.newBuilder()
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

}
