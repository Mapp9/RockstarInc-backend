package com.rockstarinc.RIecom.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;





@Component
public class JwtUtil {

    // Clave secreta utilizada para firmar y verificar el JWT
    public static final String SECRET = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";

    // Genera un token JWT para un nombre de usuario dado
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    // Crea el token JWT con las reclamaciones proporcionadas y el nombre de usuario
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
            .setClaims(claims) // Establece las reclamaciones en el JWT
            .setSubject(userName) // Establece el sujeto(nombre de usuario)
            .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión del token
            .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 30)) // Fecha de expiración del token
            .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // Firma el token con la clave secreta
    }
    // Devuelve la clave de firma para el JWT
    private Key getSignKey() {
        byte[] keybytes = Decoders.BASE64.decode(SECRET); // Decodifica la clave secreta
        return Keys.hmacShaKeyFor(keybytes); // Crea una clave HMAC para firmar
    }
    // Extrae el nombre de usuario del token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // Extrae una reclamación específica del token JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // Extrae todas las reclamaciones del token JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }
    // Verifica si el token ha expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // Extrae la fecha de expiración del token JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // Valida el token JWT comparando el nombre de usuario y verificando que no haya expirado
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
