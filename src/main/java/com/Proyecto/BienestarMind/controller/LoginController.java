package com.Proyecto.BienestarMind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Usamos @Controller para servir vistas (Thymeleaf/HTML)
public class LoginController {

    // Este método mapea la URL /login a la vista login.html
    @GetMapping("/login")
    public String login() {
        return "login"; // Nombre del archivo HTML en src/main/resources/templates/
    }
    
    // Controlador para /home (éxito después del login)
    @GetMapping("/home")
    public String home() {
        return "home"; // Debes crear home.html
    }
}