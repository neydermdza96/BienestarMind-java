package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Elementos;
import com.Proyecto.BienestarMind.repository.ElementosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElementosService {

    @Autowired
    private ElementosRepository elementosRepository;

    public List<Elementos> findAll() {
        return elementosRepository.findAll();
    }

    public Optional<Elementos> findById(Integer id) {
        return elementosRepository.findById(id);
    }

    public Elementos save(Elementos elemento) {
        return elementosRepository.save(elemento);
    }

    public void deleteById(Integer id) {
        elementosRepository.deleteById(id);
    }
}
