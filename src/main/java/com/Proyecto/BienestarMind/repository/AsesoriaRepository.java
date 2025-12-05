package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // ✅ CORRECCIÓN DE IMPORT
import java.util.List;
import java.util.Date;
import java.time.LocalDate;

@Repository
public interface AsesoriaRepository extends JpaRepository<Asesoria, Integer>{

    
    @Query("SELECT a FROM Asesoria a " +
           "LEFT JOIN FETCH a.usuarioRecibe " +
           "LEFT JOIN FETCH a.usuarioAsesor " +
           "LEFT JOIN FETCH a.ficha")
    List<Asesoria> findAllWithDetails();
    
    @Transactional
    void deleteByUsuarioRecibe(Usuario usuario);

    @Transactional
    void deleteByUsuarioAsesor(Usuario usuario);
}