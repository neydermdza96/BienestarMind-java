package com.Proyecto.BienestarMind.repository;

import com.Proyecto.BienestarMind.model.Asesoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsesoriaRepository extends JpaRepository<Asesoria, Integer> {
    // Aquí puedes añadir métodos de consulta personalizados, por ejemplo:
    // List<Asesoria> findByFechaAfter(LocalDate fecha);
}