package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.ReservaElementos;
import com.Proyecto.BienestarMind.repository.ReservaElementosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservaElementosService {

    @Autowired
    private ReservaElementosRepository reservaElementosRepository;

    // Límite global para CREAR nuevas reservas
    private static final LocalDate FECHA_LIMITE = LocalDate.of(2025, 12, 17);

    // Festivos Colombia 2025
    private static final Set<LocalDate> DIAS_FESTIVOS_2025 = Set.of(
            LocalDate.of(2025, 1, 1), // Año Nuevo
            LocalDate.of(2025, 1, 6), // Reyes Magos
            LocalDate.of(2025, 3, 24), // San José (lunes)
            LocalDate.of(2025, 4, 17), // Jueves Santo
            LocalDate.of(2025, 4, 18), // Viernes Santo
            LocalDate.of(2025, 5, 1), // Día del Trabajo
            LocalDate.of(2025, 6, 2), // Ascensión (lunes)
            LocalDate.of(2025, 6, 23), // Corpus Christi (lunes)
            LocalDate.of(2025, 6, 30), // Sagrado Corazón (lunes)
            LocalDate.of(2025, 7, 20), // Independencia
            LocalDate.of(2025, 8, 7), // Batalla de Boyacá
            LocalDate.of(2025, 8, 18), // Asunción (lunes)
            LocalDate.of(2025, 10, 13), // Día de la Raza (lunes)
            LocalDate.of(2025, 11, 3), // Todos los Santos (lunes)
            LocalDate.of(2025, 11, 17), // Indep. Cartagena (lunes)
            LocalDate.of(2025, 12, 8), // Inmaculada Concepción
            LocalDate.of(2025, 12, 25) // Navidad
    );

    // ✅ CORREGIDO: Llama al método optimizado del Repositorio
    public List<ReservaElementos> findAll() {
        return reservaElementosRepository.findAllWithDetails();
    }

    public Optional<ReservaElementos> findById(Integer id) {
        return reservaElementosRepository.findById(id);
    }

    public ReservaElementos save(ReservaElementos reservaElementos) {
        // esNueva = true si el id es null (creación), false si es edición
        boolean esNueva = (reservaElementos.getIdReservaElemento() == null);
        validarFechaReserva(reservaElementos.getFechaReserva(), esNueva);
        return reservaElementosRepository.save(reservaElementos);
    }

    public void deleteById(Integer id) {
        reservaElementosRepository.deleteById(id);
    }

    // ---------- LÓGICA DE NEGOCIO PARA FECHAS ----------

    private void validarFechaReserva(LocalDate fecha, boolean esNueva) {

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de la reserva es obligatoria.");
        }

        // NO permitir reservas (ni nuevas ni editadas) en fechas anteriores a hoy
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "No se pueden guardar reservas en fechas anteriores a la fecha actual.");
        }

        // Solo para CREAR nuevas reservas: no permitir después del 17 de diciembre de
        // 2025
        if (esNueva && fecha.isAfter(FECHA_LIMITE)) {
            throw new IllegalArgumentException(
                    "No se pueden crear reservas después del 17 de diciembre de 2025.");
        }

        // No permitir domingos
        if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(
                    "No se pueden crear reservas los domingos.");
        }

        // No permitir días festivos
        if (DIAS_FESTIVOS_2025.contains(fecha)) {
            throw new IllegalArgumentException(
                    "No se pueden crear reservas en días festivos.");
        }
    }

    public List<ReservaElementos> listarTodos() {
        return reservaElementosRepository.findAll();
    }

    public List<ReservaElementos> filtrarReservaElementos(String descripcion, LocalDate desde, LocalDate hasta) {
        List<ReservaElementos> todos = reservaElementosRepository.findAll();

        return todos.stream()
                .filter(c -> descripcion == null
                        || c.getDescripcionReserva().toLowerCase().contains(descripcion.toLowerCase()))
                .filter(c -> {
                    if (desde != null && hasta != null) {
                        return !c.getFechaReserva().isBefore(desde) &&
                                !c.getFechaReserva().isAfter(hasta);
                    } else if (desde != null) {
                        return !c.getFechaReserva().isBefore(desde);
                    } else if (hasta != null) {
                        return !c.getFechaReserva().isAfter(hasta);
                    }
                    return true;
                })

                .collect(Collectors.toList());
    }

}
