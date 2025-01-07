package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sa.appexamelaboratorio.model.Exame;
import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.repository.ExameRepository;

@Service
public class ExameService {

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Exame salvarExame(Exame exame) {
        return exameRepository.save(exame);
    }

    public List<Exame> listarExames() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> user = usuarioService.buscarPorEmail(email);
        return exameRepository.findByUsuarioId(user.get().getId_usuario());
    }

    public Exame buscarPorId(Long id) {
        Optional<Exame> exame = exameRepository.findById(id);
        return exame.orElse(null);
    }

    public void deletarPorId(Long id) {
        exameRepository.deleteById(id);
    }

    public List<Exame> buscarPorPacienteELaboratorio(Long pacienteId, Long laboratorioId) {
        return exameRepository.findByPacienteIdAndLaboratorioId(pacienteId, laboratorioId);
    }

    public List<Exame> buscarPorPaciente(Long pacienteId) {
        return exameRepository.findByPacienteId(pacienteId);
    }

    public List<Exame> buscarPorLaboratorio(Long laboratorioId) {
        return exameRepository.findByLaboratorioId(laboratorioId);
    }

    public List<Exame> buscarPorNome(String nome) {
        return exameRepository.findByNomeStartingWithIgnoreCase(nome);
    }
}
