package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return laboratorioRepository.findByUsuarioId(user.get().getId());
    }

    public Laboratorio buscarPorId(Long id) {
        Optional<Laboratorio> laboratorio = laboratorioRepository.findById(id);
        return laboratorio.get();
    }

    public void deletarPorId(Long id) {
        laboratorioRepository.deleteById(id);
    }

    @Transactional
    public void deletarPeloUsuario(Long userId) {
        laboratorioRepository.deleteByUsuarioId(userId);
    }

    public List<Laboratorio> buscarPorNome(String nome) {
        return laboratorioRepository.findByNomeStartingWithIgnoreCase(nome);
    }

    public Long contarLaboratoriosUsuario() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return laboratorioRepository.countByUsuarioId(usuario.getId());
    }

    public Long contarLaboratorios() {
        return laboratorioRepository.count();
    }

    public Long contarPorId(Long id) {
        return laboratorioRepository.countByUsuarioId(id);
    }
}
