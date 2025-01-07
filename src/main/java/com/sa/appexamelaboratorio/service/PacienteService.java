package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sa.appexamelaboratorio.model.Paciente;
import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.repository.PacienteRepository;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Paciente salvarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarPacientes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> user = usuarioService.buscarPorEmail(email);
        return pacienteRepository.findByUsuarioId(user.get().getId_usuario());
    }

    public Paciente buscarPorId(Long id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.get();
    }

    public void deletarPorId(Long id) {
        pacienteRepository.deleteById(id);
    }

    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeStartingWithIgnoreCase(nome);
    }
}
