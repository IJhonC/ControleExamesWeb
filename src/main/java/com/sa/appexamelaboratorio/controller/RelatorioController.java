package com.sa.appexamelaboratorio.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sa.appexamelaboratorio.model.Exame;
import com.sa.appexamelaboratorio.model.Laboratorio;
import com.sa.appexamelaboratorio.model.Paciente;
import com.sa.appexamelaboratorio.service.ExameService;
import com.sa.appexamelaboratorio.service.LabService;
import com.sa.appexamelaboratorio.service.PacienteService;
import com.sa.appexamelaboratorio.service.RelatorioService;

@RequestMapping("/relatorio")
@Controller
public class RelatorioController {
    @Autowired
    private ExameService exameService;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private LabService labService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public String mostrarTela(Model model) {
        List<Laboratorio> laboratorios = labService.listarLab();
        List<Paciente> pacientes = pacienteService.listarPacientes();

        model.addAttribute("pacientes", pacientes);
        model.addAttribute("laboratorios", laboratorios);
        return "relatorio";
    }

    @GetMapping("/buscar")
    public String buscarExamesPorPacienteELaboratorio(
            @RequestParam("paciente") Long paciente,
            @RequestParam("laboratorio") Long laboratorio,
            Model model,
            RedirectAttributes redirectAttributes) {
        List<Exame> exames = null;

        if (paciente == 0 && laboratorio == 0) {
            redirectAttributes.addFlashAttribute("error", "Selecione ao menos um paciente ou um laboratório!");
            return "redirect:/relatorio";
        }

        if (paciente == 0) {
            exames = exameService.buscarPorLaboratorio(laboratorio);
        } else if (laboratorio == 0) {
            exames = exameService.buscarPorPaciente(paciente);
        } else {
            exames = exameService.buscarPorPacienteELaboratorio(paciente, laboratorio);
        }

        model.addAttribute("exames", exames);
        model.addAttribute("pacienteId", paciente);
        model.addAttribute("laboratorioId", laboratorio);

        return "relatorioResultado";
    }

    @GetMapping("/exportar-pdf")
    public ResponseEntity<byte[]> exportarExamesParaPdf(@RequestParam(name = "pacienteId") Long pacienteId,
            @RequestParam(name = "laboratorioId") Long laboratorioId) {
        List<Exame> exames;

        if (pacienteId == 0) {
            exames = exameService.buscarPorLaboratorio(laboratorioId);
        } else if (laboratorioId == 0) {
            exames = exameService.buscarPorPaciente(pacienteId);
        } else {
            exames = exameService.buscarPorPacienteELaboratorio(pacienteId, laboratorioId);
        }

        ByteArrayOutputStream pdf = relatorioService.exportarExamesParaPdf(exames);

        // Configura o cabeçalho e o conteúdo da resposta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio_exames.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.toByteArray());
    }

    @GetMapping("/kanban")
    public String mostrarKanban(Model model) {
        List<Exame> exames = exameService.listarExames();
        model.addAttribute("exames", exames);
        return "kanban";
    }

}
