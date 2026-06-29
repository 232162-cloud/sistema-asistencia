package com.unamba.asistencia.controller;

import com.unamba.asistencia.dto.AuthResponse;
import com.unamba.asistencia.model.Usuario;
import com.unamba.asistencia.repository.UsuarioRepository;
import com.unamba.asistencia.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(request.username());
        if (optionalUsuario.isEmpty()) {
            return unauthorized("TOKEN_INVALID");
        }

        Usuario usuario = optionalUsuario.get();
        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            return unauthorized("TOKEN_INVALID");
        }

        String token = jwtService.generarToken(usuario);
        AuthResponse response = new AuthResponse(
                token,
                usuario.getUsername(),
                usuario.getRol(),
                jwtService.generarFechaEmision(),
                jwtService.generarFechaExpiracion()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return unauthorized("UNAUTHORIZED");
        }

        String token = authorizationHeader.substring(7);
        try {
            String username = jwtService.getUsername(token);
            Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(username);
            if (optionalUsuario.isEmpty()) {
                return unauthorized("TOKEN_INVALID");
            }

            Usuario usuario = optionalUsuario.get();
            String newToken = jwtService.generarToken(usuario);
            AuthResponse response = new AuthResponse(
                    newToken,
                    usuario.getUsername(),
                    usuario.getRol(),
                    jwtService.generarFechaEmision(),
                    jwtService.generarFechaExpiracion()
            );
            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException ex) {
            return unauthorized("TOKEN_EXPIRED");
        } catch (Exception ex) {
            return unauthorized("TOKEN_INVALID");
        }
    }

    private ResponseEntity<Map<String, String>> unauthorized(String error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", error));
    }

    private record AuthRequest(String username, String password) {
    }
}
