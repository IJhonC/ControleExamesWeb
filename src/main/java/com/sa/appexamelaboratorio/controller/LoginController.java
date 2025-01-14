package com.sa.appexamelaboratorio.controller;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.service.UsuarioService;

@Controller
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String exibirLoginPage() {
       
        Usuario usuario = new Usuario();

        usuario.setNome("Adiministrador");
        usuario.setCpf("123.456.789-00");
        usuario.setEmail("admin@admin.com");
        usuario.setSexo("Masculino");
        usuario.setSenha("teste");
        usuario.setRole("ADMIN");

        usuario.setDataNascimento(new Date(90, 5, 15));

        Optional<Usuario> user = usuarioService.buscarPorEmail(usuario.getEmail());

        if (!user.isPresent()) {
            usuarioService.salvarUsuario(usuario);
        }

        return "login";
    }
}
