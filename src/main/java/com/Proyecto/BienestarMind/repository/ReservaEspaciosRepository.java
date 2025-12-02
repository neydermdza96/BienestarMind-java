package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.ReservaEspacios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar Query
import org.springframework.stereotype.Repository;
import java.util.List; // Importar List

@Repository
public interface ReservaEspaciosRepository extends JpaRepository<ReservaEspacios, Integer> {
    
    // ✅ CORRECCIÓN N+1: Carga inmediata de Usuario, Ficha y Espacio
    @Query("SELECT r FROM ReservaEspacios r " +
           "LEFT JOIN FETCH r.usuario " +
           "LEFT JOIN FETCH r.ficha " +
           "LEFT JOIN FETCH r.espacio")
    List<ReservaEspacios> findAllWithDetails();
}