package com.sa.appexamelaboratorio.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.sa.appexamelaboratorio.model.Exame;
import com.sa.appexamelaboratorio.model.Laboratorio;
import com.sa.appexamelaboratorio.model.Paciente;
import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.service.ExameService;
import com.sa.appexamelaboratorio.service.LabService;
import com.sa.appexamelaboratorio.service.PacienteService;
import com.sa.appexamelaboratorio.service.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/exame")
@Controller
public class ExameController {
    @Autowired
    private ExameService exameService;

    @Autowired
    private LabService labService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        List<Laboratorio> laboratorios = labService.listarLab();
        List<Paciente> pacientes = pacienteService.listarPacientes();

        model.addAttribute("pacientes", pacientes);
        model.addAttribute("laboratorios", laboratorios);
        model.addAttribute("exame", new Exame());
        return "cadExame";
    }

    @PostMapping("/cadastrar")
    public String cadastrarExame(Exame exame, RedirectAttributes redirectAttributes) {
        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> user = usuarioService.buscarPorEmail(emailUser);
        exame.setUsuario(user.get());
        exameService.salvarExame(exame);
        redirectAttributes.addFlashAttribute("successMessage", "Exame cadastrado com sucesso!");
        return "redirect:/exame/cadastrar";
    }

    @GetMapping("/listar")
    public String listarExames(Model model) {
        model.addAttribute("exame", exameService.listarExames());
        return "listExame";
    }

    @PostMapping("/deletar/{id}")
    public String deletarExame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        exameService.deletarPorId(id);
        redirectAttributes.addFlashAttribute("successMessage", "Exame excluído com sucesso!");
        return "redirect:/exame/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Exame exame = exameService.buscarPorId(id);
        if (exame != null) {
            List<Laboratorio> laboratorios = labService.listarLab();
            List<Paciente> pacientes = pacienteService.listarPacientes();
            LocalDate data = exame.getData();
            laboratorios.remove(exame.getLaboratorio());
            pacientes.remove(exame.getPaciente());

            model.addAttribute("pacientes", pacientes);
            model.addAttribute("laboratorios", laboratorios);
            model.addAttribute("exame", exame);
            model.addAttribute("data", data);

            System.out.println("DATA DO EXAME: " + exame.getData());
            return "editarExame";
        } else {
            return "redirect:/exame/listar";
        }
    }

    @PostMapping("/editar")
    public String editarExame(@ModelAttribute Exame exame, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Exame alterado com sucesso!");
        exameService.salvarExame(exame);
        return "redirect:/exame/listar";
    }

    @PostMapping("/mudarStatus/{id}/{mudarPara}")
    public String alterarStauts(@PathVariable Long id, @PathVariable String mudarPara) {
        Exame exame = exameService.buscarPorId(id);
        exame.setStatus(mudarPara);
        exameService.salvarExame(exame);
        return "redirect:/relatorio/kanban";
    }
}
