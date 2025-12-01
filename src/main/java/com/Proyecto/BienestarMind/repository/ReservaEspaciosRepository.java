package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.ReservaEspacios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaEspaciosRepository extends JpaRepository<ReservaEspacios, Integer> {
}
