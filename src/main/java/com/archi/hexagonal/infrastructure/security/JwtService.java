package com.archi.hexagonal.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/*
Le JWT contient l’identité de l’utilisateur, une date d’expiration et
est signé avec une clé secrète. À chaque requête, le backend vérifie
la signature et l’expiration sans appeler la base de données,
ce qui rend l’API stateless.
 */
@Service
public class JwtService {
    @Value(value = "${app.jwt-secret}")
    private String jwtSecret;

    @Value(value = "${app.jwt-expiration-in-ms}")
    private int jwtExpirationInMs;

    //Le token contient : user + expiration
    //Méthode appelée après authentification réussie pour generer le token
    public String generateToken(String username, Collection<String> roles){
        return Jwts.builder()
                .setSubject(username)
                .claim("roles",roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationInMs))  //1h
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    //Convertit la clé String en clé crypto
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    //Permet de récupérer l’utilisateur depuis le JWT
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    //Méthode appelée par le filtre JWT
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Méthode centrale de validation
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
