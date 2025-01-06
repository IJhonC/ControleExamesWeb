package com.sa.appexamelaboratorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.appexamelaboratorio.model.Laboratorio;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
     List<Laboratorio> findByUsuarioId(Long userId);
}
