package com.unamba.asistencia.config;

import com.unamba.asistencia.model.Usuario;
import com.unamba.asistencia.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ApplicationRunner seedAdminUsuario() {
        return args -> {
            String username = "admin";
            if (usuarioRepository.findByUsername(username).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername(username);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol("ADMIN");
                usuarioRepository.save(admin);
            }
        };
    }
}
