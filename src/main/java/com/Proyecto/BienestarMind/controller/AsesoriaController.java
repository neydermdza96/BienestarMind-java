package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.service.AsesoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException; // Importación necesaria
import java.util.List;

@RestController
@RequestMapping("/api/asesorias")
public class AsesoriaController {

    @Autowired
    private AsesoriaService asesoriaService;

    // ... (Métodos GET, PUT, DELETE son correctos en su estructura principal) ...

    // GET /api/asesorias -> Obtener todas las asesorías
    @GetMapping
    public List<Asesoria> getAll() {
        return asesoriaService.findAll();
    }

    // GET /api/asesorias/{id} -> Obtener asesoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Asesoria> getById(@PathVariable Integer id) {
        return asesoriaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/asesorias -> Crear una nueva asesoría
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Asesoria create(@RequestBody Asesoria asesoria) {
        // La validación de la fecha se ejecuta en el servicio. 
        // Si falla, el @ExceptionHandler lo capturará.
        return asesoriaService.save(asesoria);
    }

    // PUT /api/asesorias/{id} -> Actualizar una asesoría
    @PutMapping("/{id}")
    public ResponseEntity<Asesoria> update(@PathVariable Integer id, @RequestBody Asesoria asesoriaDetails) {
        return asesoriaService.findById(id)
                .map(existingAsesoria -> {
                    // Actualiza solo los campos modificables
                    existingAsesoria.setMotivoAsesoria(asesoriaDetails.getMotivoAsesoria());
                    existingAsesoria.setFecha(asesoriaDetails.getFecha());
                    // Nota: Idealmente, solo se actualizan los campos necesarios
                    // Aquí asumimos que los IDs de las relaciones vienen en el JSON
                    existingAsesoria.setUsuarioRecibe(asesoriaDetails.getUsuarioRecibe());
                    existingAsesoria.setUsuarioAsesor(asesoriaDetails.getUsuarioAsesor());
                    existingAsesoria.setFicha(asesoriaDetails.getFicha());

                    // La validación de la fecha se ejecuta en el servicio.
                    Asesoria updatedAsesoria = asesoriaService.save(existingAsesoria);
                    return ResponseEntity.ok(updatedAsesoria);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/asesorias/{id} -> Eliminar una asesoría (Delete)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        // Si la asesoría tiene registros relacionados, podría lanzar DataIntegrityViolationException.
        asesoriaService.deleteById(id);
    }

    // =======================================================
    //          MANEJO DE EXCEPCIONES CENTRALIZADO
    // =======================================================

    /**
     * ✅ Maneja errores de reglas de negocio (ej: fecha inválida, no festivos).
     * Devuelve HTTP 400 (Bad Request).
     * @param ex La excepción lanzada desde la capa de servicio.
     * @return ResponseEntity con el mensaje de error y código 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // El mensaje de la excepción es el que generaste en el AsesoriaService
        return new ResponseEntity<>("Error de validación: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    /**
     * ✅ Maneja errores de la base de datos (ej: intentar borrar un registro que tiene dependencias).
     * Devuelve HTTP 409 (Conflict).
     * @param ex La excepción de integridad de datos.
     * @return ResponseEntity con el mensaje de error y código 409.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Error de integridad de datos: No se puede eliminar o modificar el registro debido a dependencias en la base de datos.", HttpStatus.CONFLICT);
    }
}