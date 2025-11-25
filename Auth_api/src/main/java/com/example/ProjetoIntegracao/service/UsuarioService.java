package com.example.ProjetoIntegracao.service;

import com.example.ProjetoIntegracao.model.UsuarioModel;
import com.example.ProjetoIntegracao.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioModel registrar(UsuarioModel usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel autenticar(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(senha, user.getSenha()))
                .orElse(null);
    }
}
