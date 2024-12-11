package com.sa.appexamelaboratorio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.appexamelaboratorio.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}