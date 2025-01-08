package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sa.appexamelaboratorio.model.Laboratorio;
import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.repository.LaboratorioRepository;

@Service
public class LabService {
    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Laboratorio salvarLab(Laboratorio laboratorio) {
        return laboratorioRepository.save(laboratorio);
    }

    public List<Laboratorio> listarLab() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> user = usuarioService.buscarPorEmail(email);
        return laboratorioRepository.findByUsuarioId(user.get().getId_usuario());
    }

    public Laboratorio buscarPorId(Long id) {
        Optional<Laboratorio> laboratorio = laboratorioRepository.findById(id);
        return laboratorio.get();
    }

    public void deletarPorId(Long id) {
        laboratorioRepository.deleteById(id);
    }

    public List<Laboratorio> buscarPorNome(String nome) {
        return laboratorioRepository.findByNomeStartingWithIgnoreCase(nome);
    }

    public Long contarLaboratorios(){
        return laboratorioRepository.count();
    }
}
