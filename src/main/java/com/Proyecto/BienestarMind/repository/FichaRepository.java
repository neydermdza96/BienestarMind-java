package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Importante: La clave primaria de Ficha es String, no Integer.
public interface FichaRepository extends JpaRepository<Ficha, String> {
    
}