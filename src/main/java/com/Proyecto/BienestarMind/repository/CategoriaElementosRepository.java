package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.CategoriaElementos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaElementosRepository extends JpaRepository<CategoriaElementos, Integer> {
}
