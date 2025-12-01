package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.ReservaElementos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaElementosRepository extends JpaRepository<ReservaElementos, Integer> {
}
