package com.sa.appexamelaboratorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.appexamelaboratorio.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByUsuarioId(Long userId);

    List<Paciente> findByNomeStartingWithIgnoreCase(String nome);

    Long countByUsuarioId(Long userId);

    void deleteByUsuarioId(Long userId);
}