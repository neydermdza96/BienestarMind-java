package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Integer> {
}