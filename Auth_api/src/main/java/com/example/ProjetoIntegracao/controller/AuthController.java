package com.example.ProjetoIntegracao.controller;

import com.example.ProjetoIntegracao.auth.JwtUtil;
import com.example.ProjetoIntegracao.model.UsuarioModel;
import com.example.ProjetoIntegracao.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioModel> registrar(@RequestBody UsuarioModel usuario) {
        UsuarioModel novoUsuario = usuarioService.registrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioModel usuario) {
        UsuarioModel autenticado = usuarioService.autenticar(usuario.getEmail(), usuario.getSenha());
        if (autenticado == null) {
            return ResponseEntity.status(401).body(Map.of("erro", "Credenciais inválidas"));
        }

        String token = jwtUtil.generateToken(autenticado.getEmail());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/validacao")
    public ResponseEntity<String> validacao(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        boolean validate = jwtUtil.validateToken(token);
        if (validate){
            return ResponseEntity.ok("Autorizado");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não Autorizado");
        }
    }



}
