package com.Proyecto.BienestarMind.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (puede que lo necesites habilitar en prod)
            .authorizeHttpRequests(auth -> auth
                // Permite acceso sin autenticación al login y recursos estáticos
                .requestMatchers(new AntPathRequestMatcher("/login"), 
                                 new AntPathRequestMatcher("/css/**"), 
                                 new AntPathRequestMatcher("/js/**")).permitAll()
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Si tienes una página de login Thymeleaf
                .defaultSuccessUrl("/home", true) // Redirige a /home después del login
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll());

        return http.build();
    }

    // Define el PasswordEncoder (BCrypt es el estándar)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}