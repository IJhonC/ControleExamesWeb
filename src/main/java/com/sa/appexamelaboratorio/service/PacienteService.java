package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sa.appexamelaboratorio.model.Paciente;
import com.sa.appexamelaboratorio.repository.PacienteRepository;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    public Paciente salvarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.get();
    }

    public void deletarPorId(Long id)    {
        pacienteRepository.deleteById(id);
    }
}
