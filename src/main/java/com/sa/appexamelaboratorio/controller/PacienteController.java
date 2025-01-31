package com.sa.appexamelaboratorio.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sa.appexamelaboratorio.model.Paciente;
import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.service.PacienteService;
import com.sa.appexamelaboratorio.service.UsuarioService;

import org.springframework.ui.Model;

@RequestMapping("/paciente")
@Controller
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "cadPacientes";
    }

    @PostMapping("/cadastrar")
    public String cadastrarPaciente(Paciente paciente, RedirectAttributes redirectAttributes) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> user = usuarioService.buscarPorEmail(email);
        paciente.setUsuario(user.get());
        pacienteService.salvarPaciente(paciente);
        redirectAttributes.addFlashAttribute("successMessage", "Paciente cadastrado com sucesso!");
        return "redirect:/paciente/cadastrar";
    }

    @GetMapping("/listar")
    public String listarPacienteComParametro(Model model,
            @RequestParam(required = false) String busca) {
        if (busca == null) {
            model.addAttribute("paciente", pacienteService.listarPacientes());
            return "listPaciente";
        }
        model.addAttribute("paciente", pacienteService.buscarPorNome(busca));
        return "listPaciente";
    }

    @PostMapping("/deletar/{id}")
    public String deletarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pacienteService.deletarPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Paciente excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Não é possível excluir o paciente, pois ele possui exames associados!");
        }
        return "redirect:/paciente/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente != null) {
            model.addAttribute("paciente", paciente);
            return "editarPaciente";
        } else {
            return "redirect:/paciente/listar";
        }
    }

    @PostMapping("/editar")
    public String editarPaciente(@ModelAttribute Paciente paciente, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Paciente alterado com sucesso!");
        pacienteService.salvarPaciente(paciente);
        return "redirect:/paciente/listar";
    }
}