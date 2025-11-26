package com.Proyecto.BienestarMind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    /**
     * Maneja la URL raíz (/) y la ruta /index para mostrar la página de inicio pública.
     * Retorna "index", que se resuelve a src/main/resources/templates/index.html.
     * Esta ruta está permitida por Spring Security (ver paso 2).
     */
    @GetMapping({"/", "/index"})
    public String viewIndex() {
        return "index"; 
    }
}