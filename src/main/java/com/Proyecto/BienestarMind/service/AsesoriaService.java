package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.repository.AsesoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsesoriaService {

    @Autowired
    private AsesoriaRepository asesoriaRepository;

    // Obtener todas las asesorías
    public List<Asesoria> findAll() {
        return asesoriaRepository.findAll();
    }

    // Obtener asesoría por ID
    public Optional<Asesoria> findById(Integer id) {
        return asesoriaRepository.findById(id);
    }

    // Guardar o actualizar una asesoría
    public Asesoria save(Asesoria asesoria) {
        // **Lógica de Negocio:** Aquí podrías añadir validaciones.
        return asesoriaRepository.save(asesoria);
    }

    // Eliminar una asesoría
    public void deleteById(Integer id) {
        asesoriaRepository.deleteById(id);
    }
}