package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Ficha;
import com.Proyecto.BienestarMind.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    // Obtener todas las Fichas
    public List<Ficha> findAll() {
        return fichaRepository.findAll();
    }
    
    // Obtener Ficha por ID (opcional)
    public Optional<Ficha> findById(String id) {
        return fichaRepository.findById(id);
    }

    // Método para guardar una Ficha
    public Ficha save(Ficha ficha) {
        return fichaRepository.save(ficha);
    }
    
    // ... otros métodos CRUD según se necesite
}