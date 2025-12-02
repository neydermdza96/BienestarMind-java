package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.ReservaEspacios;
import com.Proyecto.BienestarMind.repository.ReservaEspaciosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReservaEspaciosService {

    @Autowired
    private ReservaEspaciosRepository reservaEspaciosRepository;

    // Límite global para CREAR nuevas reservas
    private static final LocalDate FECHA_LIMITE = LocalDate.of(2025, 12, 17);

    // Festivos Colombia 2025 (inmutable, compatible con Java 8+)
    private static final Set<LocalDate> DIAS_FESTIVOS_2025;
    static {
        Set<LocalDate> tmp = new HashSet<>();
        tmp.add(LocalDate.of(2025, 1, 1));   // Año Nuevo
        tmp.add(LocalDate.of(2025, 1, 6));   // Reyes Magos
        tmp.add(LocalDate.of(2025, 3, 24));  // San José (lunes)
        tmp.add(LocalDate.of(2025, 4, 17));  // Jueves Santo
        tmp.add(LocalDate.of(2025, 4, 18));  // Viernes Santo
        tmp.add(LocalDate.of(2025, 5, 1));   // Día del Trabajo
        tmp.add(LocalDate.of(2025, 6, 2));   // Ascensión (lunes)
        tmp.add(LocalDate.of(2025, 6, 23));  // Corpus Christi (lunes)
        tmp.add(LocalDate.of(2025, 6, 30));  // Sagrado Corazón (lunes)
        tmp.add(LocalDate.of(2025, 7, 20));  // Independencia
        tmp.add(LocalDate.of(2025, 8, 7));   // Batalla de Boyacá
        tmp.add(LocalDate.of(2025, 8, 18));  // Asunción (lunes)
        tmp.add(LocalDate.of(2025, 10, 13)); // Día de la Raza (lunes)
        tmp.add(LocalDate.of(2025, 11, 3));  // Todos los Santos (lunes)
        tmp.add(LocalDate.of(2025, 11, 17)); // Indep. Cartagena (lunes)
        tmp.add(LocalDate.of(2025, 12, 8));  // Inmaculada Concepción
        tmp.add(LocalDate.of(2025, 12, 25)); // Navidad
        DIAS_FESTIVOS_2025 = Collections.unmodifiableSet(tmp);
    }

    public List<ReservaEspacios> findAll() {
        return reservaEspaciosRepository.findAll();
    }

    public Optional<ReservaEspacios> findById(Integer id) {
        return reservaEspaciosRepository.findById(id);
    }

    public ReservaEspacios save(ReservaEspacios reservaEspacios) {
        boolean esNueva = (reservaEspacios.getIdReservaEspacio() == null);
        validarFechaReserva(reservaEspacios.getFechaReserva(), esNueva);
        return reservaEspaciosRepository.save(reservaEspacios);
    }

    public void deleteById(Integer id) {
        reservaEspaciosRepository.deleteById(id);
    }

    // ---------- LÓGICA DE NEGOCIO PARA FECHAS ----------

    private void validarFechaReserva(LocalDate fecha, boolean esNueva) {

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de la reserva es obligatoria.");
        }

        //  NO permitir reservas (ni nuevas ni editadas) en fechas anteriores a hoy
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "No se pueden guardar reservas en fechas anteriores a la fecha actual."
            );
        }

        // Solo para CREAR nuevas reservas: no permitir después del 17 de diciembre de 2025
        if (esNueva && fecha.isAfter(FECHA_LIMITE)) {
            throw new IllegalArgumentException(
                    "No se pueden crear reservas después del 17 de diciembre de 2025."
            );
        }

        //  No permitir domingos
        if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(
                    "No se pueden crear reservas los domingos."
            );
        }

        // No permitir días festivos
        if (DIAS_FESTIVOS_2025.contains(fecha)) {
            throw new IllegalArgumentException(
                    "No se pueden crear reservas en días festivos."
            );
        }
    }
}
