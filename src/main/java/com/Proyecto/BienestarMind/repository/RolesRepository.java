package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    
    // ✅ Busca por el nombre del rol tal como está en la columna "Descripcion" (Ej: "ADMINISTRADOR")
    Optional<Roles> findByNombreRol(String nombreRol);
}