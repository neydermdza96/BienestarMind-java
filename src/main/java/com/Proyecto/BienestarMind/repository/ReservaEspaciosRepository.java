package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.ReservaEspacios;
import com.Proyecto.BienestarMind.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // ✅ CORRECCIÓN DE IMPORT
import java.util.List;

@Repository
public interface ReservaEspaciosRepository extends JpaRepository<ReservaEspacios, Integer> {
    
    @Query("SELECT r FROM ReservaEspacios r " +
           "LEFT JOIN FETCH r.usuario " +
           "LEFT JOIN FETCH r.ficha " +
           "LEFT JOIN FETCH r.espacio")
    List<ReservaEspacios> findAllWithDetails();

    @Transactional
    void deleteByUsuario(Usuario usuario);
}