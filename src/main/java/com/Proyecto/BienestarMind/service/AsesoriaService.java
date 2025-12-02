package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.repository.AsesoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate; // Importación necesaria
import java.util.List;
import java.util.Optional;
import java.util.Set; // Importación necesaria

@Service
public class AsesoriaService {

    @Autowired
    private AsesoriaRepository asesoriaRepository;

    // Festivos Colombia 2025 (Declaración para la lógica de validación)
    private static final Set<LocalDate> DIAS_FESTIVOS_2025 = Set.of(
        LocalDate.of(2025, 1, 1),   // Año Nuevo
        LocalDate.of(2025, 1, 6),   // Reyes Magos
        LocalDate.of(2025, 3, 24),  // San José (lunes)
        LocalDate.of(2025, 4, 17),  // Jueves Santo
        LocalDate.of(2025, 4, 18),  // Viernes Santo
        LocalDate.of(2025, 5, 1),   // Día del Trabajo
        LocalDate.of(2025, 6, 2),   // Ascensión (lunes)
        LocalDate.of(2025, 6, 23),  // Corpus Christi (lunes)
        LocalDate.of(2025, 6, 30),  // Sagrado Corazón (lunes)
        LocalDate.of(2025, 7, 20),  // Independencia
        LocalDate.of(2025, 8, 7),   // Batalla de Boyacá
        LocalDate.of(2025, 8, 18),  // Asunción (lunes)
        LocalDate.of(2025, 10, 13), // Día de la Raza (lunes)
        LocalDate.of(2025, 11, 3),  // Todos los Santos (lunes)
        LocalDate.of(2025, 11, 17), // Indep. Cartagena (lunes)
        LocalDate.of(2025, 12, 8),  // Inmaculada Concepción
        LocalDate.of(2025, 12, 25)  // Navidad
    );

    // Obtener todas las asesorías (usando consulta optimizada)
    public List<Asesoria> findAll() {
        // Asumiendo que has añadido el método findAllWithDetails() al Repositorio
        return asesoriaRepository.findAllWithDetails(); 
    }

    // Obtener asesoría por ID
    public Optional<Asesoria> findById(Integer id) {
        return asesoriaRepository.findById(id);
    }

    // Guardar o actualizar una asesoría
    public Asesoria save(Asesoria asesoria) {
        // ✅ CORRECCIÓN: Llamada al método con el tipo correcto (LocalDate)
        validarFechaAsesoria(asesoria.getFecha()); 
        return asesoriaRepository.save(asesoria);
    }

    // Eliminar una asesoría
    public void deleteById(Integer id) {
        asesoriaRepository.deleteById(id);
    }
    
    // ✅ CORRECCIÓN DEL MÉTODO DE VALIDACIÓN: Acepta LocalDate
    private void validarFechaAsesoria(LocalDate fecha) {
        
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de la asesoría es obligatoria.");
        }
        
        // NO permitir asesorías en fechas anteriores a hoy
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "No se pueden agendar asesorías en fechas anteriores a la fecha actual."
            );
        }

        // No permitir domingos
        if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(
                    "No se pueden agendar asesorías los domingos."
            );
        }

        // No permitir días festivos
        if (DIAS_FESTIVOS_2025.contains(fecha)) {
            throw new IllegalArgumentException(
                    "No se pueden agendar asesorías en días festivos."
            );
        }
    }
}