package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Espacios;
import com.Proyecto.BienestarMind.repository.EspaciosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspaciosService {

    @Autowired
    private EspaciosRepository espaciosRepository;

    public List<Espacios> findAll() {
        return espaciosRepository.findAll();
    }

    public Optional<Espacios> findById(Integer id) {
        return espaciosRepository.findById(id);
    }

    public Espacios save(Espacios espacio) {
        return espaciosRepository.save(espacio);
    }

    public void deleteById(Integer id) {
        espaciosRepository.deleteById(id);
    }
}
