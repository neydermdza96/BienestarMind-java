package com.Proyecto.BienestarMind.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (según tu configuración original)
            
            // 1. Configuración de Autorización
            .authorizeHttpRequests(auth -> auth
                // ✅ RUTAS PÚBLICAS Y ESTÁTICAS - PERMITE ACCESO SIN LOGIN
                .requestMatchers("/", 
                                 "/index", // Home page
                                 "/login", 
                                 "/css/**", // Acceso a tu CSS
                                 "/img/**", // Acceso a las imágenes del slider/logo
                                 "/icon/**", // Acceso a los iconos de redes sociales
                                 "/css/**", 
                                 "/js/**", 
                                 "/images/**").permitAll() 
                
                // Cualquier otra solicitud (ej: /app/dashboard) requiere autenticación
                .anyRequest().authenticated()
            )
            
            // 2. Configuración de Login
            .formLogin(form -> form
                .loginPage("/login") // La página de login que creaste
                // ✅ CORRECCIÓN: Redirige al Dashboard después del éxito.
                .defaultSuccessUrl("/app/dashboard", true) 
                .permitAll()
            )
            
            // 3. Configuración de Logout
            .logout(logout -> logout
                .logoutUrl("/logout") 
                .logoutSuccessUrl("/index") // Redirige al login después de cerrar sesión
                .permitAll()
            );

        return http.build();
    }

    // Define el PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}