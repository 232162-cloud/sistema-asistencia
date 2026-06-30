package com.unamba.asistencia.security;

import com.unamba.asistencia.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "3d6f8c1b9e7a2c4d5f6b8a7c1d2e3f4a5b6c7d8e9f0a1b2c";
    private static final long EXPIRATION_TIME = 3600000L;

    public String generarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", usuario.getRol());
        claims.put("idUsuario", usuario.getId());
        claims.put("nombreUsuario", usuario.getUsername());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(generarFechaEmision())
                .setExpiration(generarFechaExpiracion())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String username = claims.getSubject();
            Date expiration = claims.getExpiration();
            return username != null && expiration != null && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Date generarFechaEmision() {
        return new Date();
    }

    public Date generarFechaExpiracion() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

private Claims extractAllClaims(String token) {
    return Jwts.parser()
            .verifyWith((javax.crypto.SecretKey) getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
}
    private Key getSignInKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
