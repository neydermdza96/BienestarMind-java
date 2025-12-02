package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Asesoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar Query
import org.springframework.stereotype.Repository;

import java.util.List; // Importar List
import java.util.Date;
import java.time.LocalDate; // Usar LocalDate

@Repository
public interface AsesoriaRepository extends JpaRepository<Asesoria, Integer>{

    
    // ✅ CORRECCIÓN N+1: Consulta optimizada para cargar relaciones
    @Query("SELECT a FROM Asesoria a " +
           "LEFT JOIN FETCH a.usuarioRecibe " +
           "LEFT JOIN FETCH a.usuarioAsesor " +
           "LEFT JOIN FETCH a.ficha")
    List<Asesoria> findAllWithDetails();
    
    // Aquí puedes añadir métodos de consulta personalizados que discutimos antes, como:
    // List<Asesoria> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    // List<Asesoria> findByUsuarioAsesor_IdUsuarioAndFechaBetween(Long idAsesor, LocalDate fechaInicio, LocalDate fechaFin);
}