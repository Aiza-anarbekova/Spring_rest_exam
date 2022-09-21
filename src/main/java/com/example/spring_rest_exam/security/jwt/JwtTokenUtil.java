package com.example.spring_rest_exam.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenUtil {
    @Value("java-6")
    private String jwtSecret;
    private final Long JWT_TOKEN_VALIDATE = 7 * 24 * 60 * 60 * 1000L;

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis())) //date current token
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDATE))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public Date getExpirationDateFromToke(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims,T> function){
        final Claims claims = getClaimsFromToken(token);
        return function.apply(claims);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToke(token);
        return expiration.before(new Date());
    }

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token,Claims::getSubject);
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
}
