package com.example.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtUtil {

    private JwtUtil() {
    }

    private static final String SECRET_KEY = ConfigUtil.getJwtSecret();  // Caricata da config.properties
    private static final long EXPIRATION_TIME = ConfigUtil.getJwtExpiration(); // Caricata da config.properties

    // Metodo per generare un token JWT
    public static String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // Metodo per validare un token JWT
    public static String validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject(); // Restituisce lo username contenuto nel token
        } catch (JWTVerificationException exception) {
            // Token non valido
            return null;
        }
    }

    // Metodo per ottenere il tempo rimanente alla scadenza del token JWT
    public static long getTimeUntilExpiration(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            Date expirationDate = jwt.getExpiresAt();
            long currentTime = System.currentTimeMillis();

            // Calcola il tempo rimanente in millisecondi
            return expirationDate.getTime() - currentTime;
        } catch (JWTVerificationException exception) {
            // Token non valido o scaduto
            return -1; // Indica che il token non è valido o è già scaduto
        }
    }
}

