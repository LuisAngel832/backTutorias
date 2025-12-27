package com.codespace.tutorias.JWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    @Value("${api.key}")
    private String apiKey;

    public String generateToken(String matricula, String rol) {

        return Jwts.builder()
                .claim("matricula", matricula)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
                .signWith(Keys.hmacShaKeyFor(apiKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(apiKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

 

    public boolean isTokenValid(String token) {
        try{
            Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(apiKey.getBytes()))
            .build()
            .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String toeken){
        Claims claims = getClaims(toeken);

        String matricula = claims.get("matricula", String.class);
        String rol = claims.get("rol", String.class);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));

        return new UsernamePasswordAuthenticationToken(matricula, null, authorities);
    }

}
