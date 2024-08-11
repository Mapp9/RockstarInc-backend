package com.rockstarinc.RIecom.filters;


import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rockstarinc.RIecom.services.jwt.UserDetailsServiceImpl;
import com.rockstarinc.RIecom.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtsRequestFilter extends OncePerRequestFilter {
    // Servicios necesarios para la autenticación
    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        // Obtener el encabezado Authorization del request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        // Verificar si el encabezado contiene un token JWT válido
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        // Validar el token y configurar el contexto de seguridad si es válido
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Cargar los detalles del usuario utilizando el nombre de usuario extraído
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Verificar si el token es válido
            if(jwtUtil.validateToken(token, userDetails)) {
                // Crear un token de autenticación y configurarlo en el contexto de seguridad
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
    
}
