package com.sa.appexamelaboratorio.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.service.UsuarioService;

@Controller
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String exibirLoginPage() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String exibirTelaCadastro() {
        return "newAccount";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(Usuario usuario, RedirectAttributes redirectAttributes) {
        // Verifica se já existe um usuário com o mesmo email
        Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(usuario.getEmail());

        if (usuarioExistente.isPresent()) {
            // Se o email já estiver cadastrado, retorna um erro
            redirectAttributes.addFlashAttribute("erro", "Este email já está cadastrado. Tente outro.");
            return "redirect:/cadastro"; // Redireciona de volta para a página de cadastro
        }
        usuarioService.salvarUsuario(usuario);
        redirectAttributes.addFlashAttribute("success", "Cadastro realizado com sucesso!");
        return "redirect:/login";
    }
}
