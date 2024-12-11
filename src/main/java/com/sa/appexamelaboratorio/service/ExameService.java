package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sa.appexamelaboratorio.model.Exame;
import com.sa.appexamelaboratorio.repository.ExameRepository;

@Service
public class ExameService {

    @Autowired
    private ExameRepository exameRepository;

    public Exame salvarExame(Exame exame) {
        return exameRepository.save(exame);
    }

    public List<Exame> listarExames() {
        return exameRepository.findAll();
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
    
    public List<Exame> buscarPorPaciente(Long pacienteId){
        return exameRepository.findByPacienteId(pacienteId);
    }

    public List<Exame> buscarPorLaboratorio(Long laboratorioId){
        return exameRepository.findByLaboratorioId(laboratorioId);
    }
}
