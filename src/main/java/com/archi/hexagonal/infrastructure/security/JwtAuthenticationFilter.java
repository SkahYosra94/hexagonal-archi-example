package com.archi.hexagonal.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /*
    Intercepte chaque requête
    Vérifie le token
    Injecte l’utilisateur dans le contexte Spring
    1️⃣ Le filtre extrait le token
2️⃣ Il valide le token
3️⃣ Il injecte l’utilisateur dans SecurityContext
4️⃣ Spring autorise ou refuse la requête
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        // ⛔ NE PAS FILTRER LE LOGIN
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (jwtService.isTokenValid(token)) {
            List<String> roles = jwtService.extractRoles(token);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableList());
            UsernamePasswordAuthenticationToken authentification = new UsernamePasswordAuthenticationToken(jwtService.extractUsername(token), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentification);
        }
        filterChain.doFilter(request, response);
    }
}
