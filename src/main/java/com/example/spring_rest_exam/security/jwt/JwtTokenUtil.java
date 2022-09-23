package com.example.spring_rest_exam.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@ConfigurationProperties(prefix = "jwt.token")
@Getter
@Setter
public class JwtTokenUtil {

    private String secret;

    private String issuer;


    private long expires;

    public String generateToken(String email) {

        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(60).toInstant());

        return JWT.create()
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    // validate token
    public String validateJWTToken(String jwt) {
        DecodedJWT verify = getDecodedJWT(jwt);

        return verify.getClaim("email").asString();
    }

    private DecodedJWT getDecodedJWT(String jwt) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build();

        return jwtVerifier.verify(jwt);
    }

    public LocalDateTime getIssuedAt(String jwt) {
        DecodedJWT decodedJWT = getDecodedJWT(jwt);

        return LocalDateTime.ofInstant(
                decodedJWT.getIssuedAt().toInstant(),
                ZoneId.systemDefault()
        );
    }

}
