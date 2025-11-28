package com.Proyecto.BienestarMind;

import com.Proyecto.BienestarMind.model.Usuario;       
import com.Proyecto.BienestarMind.service.UsuarioService; 
import org.springframework.boot.CommandLineRunner;       
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;      
import java.time.LocalDate;                             

@SpringBootApplication
public class BienestarMindApplication {



	public static void main(String[] args) {
		SpringApplication.run(BienestarMindApplication.class, args);
	}

    @Bean
    public CommandLineRunner initData(UsuarioService usuarioService) {
        return args -> {
            
            // ✅ CORRECCIÓN: Usamos findByCorreo para verificar la existencia del administrador específico.
            if (usuarioService.findByCorreo("admin@sena.edu.co") == null) {
                System.out.println("No se encontró el administrador. Creando Administrador Inicial...");
                
                Usuario admin = new Usuario();
                admin.setNombres("Admin");
                admin.setApellidos("Principal");
                admin.setDocumento("1000000000"); 
                admin.setCorreo("admin@sena.edu.co"); 
                admin.setContrasena("SenaAdmin2024*"); 
                admin.setGenero("Otro");
                admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));

                admin.setTelefono("3000000000");
                
                usuarioService.saveInitialAdmin(admin, "ADMINISTRADOR"); 
                
                System.out.println("✅ Administrador creado: admin@sena.edu.co / Contraseña: SenaAdmin2024*");
                System.out.println("⚠️ RECUERDA COMENTAR O ELIMINAR ESTE BLOQUE DESPUÉS DE LA PRIMERA EJECUCIÓN EXITOSA.");
            }
        };
    }
}