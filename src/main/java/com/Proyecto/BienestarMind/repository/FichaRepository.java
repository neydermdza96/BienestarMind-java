package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaRepository extends JpaRepository<Ficha, String> {
    // JpaRepository<Entidad, Tipo_De_ID>
    // En Ficha, el ID es String (Id_ficha)
}