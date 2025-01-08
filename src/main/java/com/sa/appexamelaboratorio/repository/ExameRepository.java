package com.sa.appexamelaboratorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.appexamelaboratorio.model.Exame;

public interface ExameRepository extends JpaRepository<Exame, Long> {
    List<Exame> findByPacienteIdAndLaboratorioId(Long pacienteId, Long laboratorioId);

    List<Exame> findByPacienteId(Long pacienteId);

    List<Exame> findByLaboratorioId(Long laboratorioId);

    List<Exame> findByUsuarioId(Long userId);

    List<Exame> findByNomeStartingWithIgnoreCase(String nome);

    Long countByStatus(String status);
}
