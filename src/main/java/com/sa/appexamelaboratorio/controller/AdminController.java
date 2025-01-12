package com.sa.appexamelaboratorio.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.service.ExameService;
import com.sa.appexamelaboratorio.service.LabService;
import com.sa.appexamelaboratorio.service.PacienteService;
import com.sa.appexamelaboratorio.service.UsuarioService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LabService labService;

    @Autowired
    private ExameService exameService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/admin-dashboard")
    public String mostraPainelDeControle(Model model, @RequestParam(required = false) String busca) {
        Long[] valores = { labService.contarLaboratorios(), pacienteService.contarPacientes(),
                exameService.contarExames() };

        Long totalUsuarios = usuarioService.contarUsuarios();
        Long totalMulheres = usuarioService.contarPorGenero("Feminino");
        Long totalHomens = usuarioService.contarPorGenero("Masculino");

        int porcentagemHomens = totalUsuarios > 0 ? (int) ((totalHomens * 100) / totalUsuarios) : 0;
        int porcentagemMulheres = totalUsuarios > 0 ? (int) ((totalMulheres * 100) / totalUsuarios) : 0;

        model.addAttribute("totalUsuarios", usuarioService.contarUsuarios());
        model.addAttribute("porcentagemHomens", porcentagemHomens);
        model.addAttribute("porcentagemMulheres", porcentagemMulheres);
        model.addAttribute("valores", valores);
        if (busca == null) {
            model.addAttribute("usuarios", usuarioService.listarUsuaios());
            return "controlPaine";
        }

        model.addAttribute("usuarios", usuarioService.buscarPorNome(busca));
        System.out.println(usuarioService.listarUsuaios());
        return "controlPaine";
    }

    @GetMapping("/cadastrarUsuario")
    public String exibirTelaCadastro() {
        return "newAccount";
    }

    @PostMapping("/cadastrarUsuario")
    public String cadastrarUsuario(Usuario usuario, RedirectAttributes redirectAttributes) {
        // Verifica se já existe um usuário com o mesmo email
        Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(usuario.getEmail());

        if (usuarioExistente.isPresent()) {
            // Se o email já estiver cadastrado, retorna um erro
            redirectAttributes.addFlashAttribute("erro", "Este email já está cadastrado. Tente outro.");
            return "redirect:/admin/admin-dashboard"; // Redireciona de volta para a página de cadastro
        }
        usuarioService.salvarUsuario(usuario);
        redirectAttributes.addFlashAttribute("success", "Cadastro realizado com sucesso!");
        return "redirect:/admin/admin-dashboard";
    }

    @GetMapping("/gerenciar/{id}")
    public String mostrarTelaGerenciamento(@PathVariable Long id, Model model) {
        Usuario user = usuarioService.buscarPorId(id);

        Long[] valores = { pacienteService.contarPorId(id), labService.contarPorId(id),
                exameService.contarExamesUsuario(id) };

        model.addAttribute("user", user);
        model.addAttribute("valores", valores);
        model.addAttribute("totalExames", exameService.contarExamesUsuario(id));
        model.addAttribute("examesPendentes", exameService.contarExamesStatusUsuario(id, "AGENDADO"));

        return "gerenciarUsuario";
    }

    @PostMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        exameService.deletarPeloUsuario(id);
        pacienteService.deletarPeloUsuario(id);
        labService.deletarPeloUsuario(id);
        usuarioService.deletarPorId(id);

        redirectAttributes.addFlashAttribute("success", "Todos os dados do Usuário foram deletados!");
        return "redirect:/admin/admin-dashboard";
    }

    @GetMapping("/trocarSenha/{id}")
    public String mostrarFormulario(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "trocarSenha";
    }

    @PostMapping("/trocarSenha")
    public String alterarSenha(@RequestParam("newPass") String novaSenha,
            @RequestParam("confirmNewPass") String confirmarSenha,
            @RequestParam("id") Long userId, RedirectAttributes redirectAttributes) {

        // Validar se as senhas coincidem
        if (!novaSenha.equals(confirmarSenha)) {
            redirectAttributes.addFlashAttribute("error", "As senhas precisam ser iguais!");
            return "redirect:/trocarSenha/" + userId; // Redireciona para uma página de erro
        }
        System.out.println("NOVA SENHA: " + novaSenha);
        Usuario user = usuarioService.buscarPorId(userId);

        user.setSenha(novaSenha);
        usuarioService.salvarUsuario(user);
        redirectAttributes.addFlashAttribute("success", "Senha Alterada com Sucesso!");
        return "redirect:/admin/admin-dashboard";
    }

}
