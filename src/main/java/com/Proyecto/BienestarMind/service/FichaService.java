package com.Proyecto.BienestarMind.service;

import com.Proyecto.BienestarMind.model.Ficha;
import com.Proyecto.BienestarMind.model.ReservaElementos;
import com.Proyecto.BienestarMind.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Ficha> listarTodos() {
        return fichaRepository.findAll();
    }

    public List<Ficha> filtrarFicha(String ficha, String programa) {
        List<Ficha> todos = fichaRepository.findAll();

        return todos.stream()
                .filter(c -> ficha == null
                        || c.getIdFicha().toLowerCase().contains(ficha.toLowerCase()))
                .filter(c -> programa == null
                        || c.getDescripcion().toLowerCase().contains(programa.toLowerCase()))
                .collect(Collectors.toList());
    }

}