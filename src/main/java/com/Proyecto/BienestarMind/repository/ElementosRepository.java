package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Elementos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementosRepository extends JpaRepository<Elementos, Integer> {
}
