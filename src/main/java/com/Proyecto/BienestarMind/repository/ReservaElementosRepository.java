package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.ReservaElementos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar Query
import org.springframework.stereotype.Repository;
import java.util.List; // Importar List

@Repository
public interface ReservaElementosRepository extends JpaRepository<ReservaElementos, Integer> {
    
    // ✅ CORRECCIÓN N+1: Carga inmediata de Usuario, Ficha y Elemento
    @Query("SELECT r FROM ReservaElementos r " +
           "LEFT JOIN FETCH r.usuario " +
           "LEFT JOIN FETCH r.ficha " +
           "LEFT JOIN FETCH r.elemento")
    List<ReservaElementos> findAllWithDetails();
}