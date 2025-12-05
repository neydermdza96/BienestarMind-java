package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
// ✅ CORRECCIÓN: Se define el tipo de la clave primaria como String.
public interface FichaRepository extends JpaRepository<Ficha, String> { 
    
    // Método usado para la eliminación en cascada manual de Usuarios.
    @Modifying
    @Transactional
    // Se usa Integer porque la columna Id_Usuario es Integer.
    @Query(value = "DELETE FROM usuario_ficha WHERE Id_Usuario = ?1", nativeQuery = true)
    void deleteUserFichaAssociations(Integer idUsuario);
}