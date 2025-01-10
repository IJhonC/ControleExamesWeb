package com.sa.appexamelaboratorio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sa.appexamelaboratorio.service.ExameService;
import com.sa.appexamelaboratorio.service.LabService;
import com.sa.appexamelaboratorio.service.PacienteService;
import com.sa.appexamelaboratorio.service.UsuarioService;

@Controller
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

        System.out.println("EXAMES: " + exameService.contarExames());
        model.addAttribute("totalPacientes", usuarioService.contarPacientes());
        model.addAttribute("valores", valores);
        if (busca == null) {
            model.addAttribute("usuarios", usuarioService.listarUsuaios());
            return "controlPaine";
        }

        model.addAttribute("usuarios", usuarioService.buscarPorNome(busca));
        System.out.println(usuarioService.listarUsuaios());
        return "controlPaine";
    }

}
