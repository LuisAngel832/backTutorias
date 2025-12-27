package com.codespace.tutorias.JWT;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JwtAuthFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                String atuhHeader = request.getHeader("Authorization");
                
                if(atuhHeader == null || !atuhHeader.startsWith("Bearer ")){
                    filterChain.doFilter(request, response);
                    return;
                }

                String token = atuhHeader.substring(7);

                
                if(!jwtUtil.isTokenValid(token)){
                    filterChain.doFilter(request, response);
                    return;
                }


                filterChain.doFilter(request, response);
    }
    
    
}
