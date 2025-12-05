package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.ReservaElementos;
import com.Proyecto.BienestarMind.model.Usuario; // ✅ NECESARIO para la eliminación de usuarios
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // ✅ NECESARIO para deleteByUsuario
import java.util.List;

@Repository
public interface ReservaElementosRepository extends JpaRepository<ReservaElementos, Integer> {
    
    // ✅ 1. Carga inmediata de Usuario, Ficha y Elemento (Resuelve LazyInitException)
    @Query("SELECT r FROM ReservaElementos r " +
           "LEFT JOIN FETCH r.usuario " +
           "LEFT JOIN FETCH r.ficha " +
           "LEFT JOIN FETCH r.elemento")
    List<ReservaElementos> findAllWithDetails();
    
    // ✅ 2. Método requerido para la eliminación en cascada manual de Usuarios
    @Transactional
    void deleteByUsuario(Usuario usuario);
}