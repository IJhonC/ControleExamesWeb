package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sa.appexamelaboratorio.model.Laboratorio;
import com.sa.appexamelaboratorio.repository.LaboratorioRepository;

@Service
public class LabService {
    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public Laboratorio salvarLab(Laboratorio laboratorio) {
        return laboratorioRepository.save(laboratorio);
    }

    public List<Laboratorio> listarLab() {
        return laboratorioRepository.findAll();
    }

    public Laboratorio buscarPorId(Long id) {
        Optional<Laboratorio> laboratorio = laboratorioRepository.findById(id);
        return laboratorio.get();
    }

    public void deletarPorId(Long id) {
        laboratorioRepository.deleteById(id);
    }
}
