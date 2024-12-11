package com.sa.appexamelaboratorio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.appexamelaboratorio.model.Laboratorio;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
    
}
