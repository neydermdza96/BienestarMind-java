package com.Proyecto.BienestarMind.controller;

import com.Proyecto.BienestarMind.model.Asesoria;
import com.Proyecto.BienestarMind.service.AsesoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asesorias")
public class AsesoriaController {

    // Inyectamos el Servicio, NO el Repositorio
    @Autowired
    private AsesoriaService asesoriaService;

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
    // Devuelve un código HTTP 201 (Created)
    @ResponseStatus(HttpStatus.CREATED)
    public Asesoria create(@RequestBody Asesoria asesoria) {
        // El servicio maneja la lógica de guardado
        return asesoriaService.save(asesoria);
    }

    // PUT /api/asesorias/{id} -> Actualizar una asesoría
    @PutMapping("/{id}")
    public ResponseEntity<Asesoria> update(@PathVariable Integer id, @RequestBody Asesoria asesoriaDetails) {
        // 1. Busca si existe la asesoría
        return asesoriaService.findById(id)
                .map(existingAsesoria -> {
                    // 2. Si existe, actualiza solo los campos modificables
                    existingAsesoria.setMotivoAsesoria(asesoriaDetails.getMotivoAsesoria());
                    existingAsesoria.setFecha(asesoriaDetails.getFecha());
                    existingAsesoria.setUsuarioRecibe(asesoriaDetails.getUsuarioRecibe());
                    existingAsesoria.setUsuarioAsesor(asesoriaDetails.getUsuarioAsesor());
                    existingAsesoria.setFicha(asesoriaDetails.getFicha());

                    // 3. Guarda y devuelve la respuesta OK (200)
                    Asesoria updatedAsesoria = asesoriaService.save(existingAsesoria);
                    return ResponseEntity.ok(updatedAsesoria);
                })
                // 4. Si no existe, devuelve 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/asesorias/{id} -> Eliminar una asesoría
    @DeleteMapping("/{id}")
    // Devuelve un código HTTP 204 (No Content)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        asesoriaService.deleteById(id);
    }
}