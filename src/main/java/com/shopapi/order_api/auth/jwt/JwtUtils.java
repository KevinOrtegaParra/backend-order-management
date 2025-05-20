/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.shopapi.order_api.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author kevin
 */
@Component
@Slf4j
public class JwtUtils {
    
    @Value("${jwt.secret.key}")
    private String secretKey;
    
    @Value("${jwt.time.expiration}")
    private String timeExpiration;
    
    //Generar token de acceso
    public String generateAccesToken(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignateKey(),SignatureAlgorithm.HS256)
                .compact();
        
    }
    
    public boolean isTokenValid(String token){
        try{
            Jwts.parser()
                    .setSigningKey(getSignateKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch(Exception e){
            log.error("Invalid token, error: ".concat(e.getMessage()));
            return false;
        }  
    }
    
    public String getUserNameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }
    
    //obtener un claim
    public <T> T getClaim(String token, Function<Claims,T> claimsFunction){
        Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
        
    }
    
    //obtener claims del token
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                    .setSigningKey(getSignateKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }
    
    public Key getSignateKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}
