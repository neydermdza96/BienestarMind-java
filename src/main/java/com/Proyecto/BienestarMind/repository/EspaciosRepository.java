package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Espacios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspaciosRepository extends JpaRepository<Espacios, Integer> {
}
