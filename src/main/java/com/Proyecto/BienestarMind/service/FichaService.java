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

    public List<Ficha> findAll() {
        return fichaRepository.findAll();
    }

    public Optional<Ficha> findById(String id) {
        return fichaRepository.findById(id);
    }

    public Ficha save(Ficha ficha) {
        return fichaRepository.save(ficha);
    }

    public void deleteById(String id) {
        fichaRepository.deleteById(id);
    }
}