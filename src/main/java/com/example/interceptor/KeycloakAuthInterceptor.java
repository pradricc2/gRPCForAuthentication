package com.example.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.*;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.ConcurrentModificationException;

import static com.example.util.Constants.KEYCLOAK_URL;
import static com.example.util.Constants.TOKEN_CERT_URL;


public class KeycloakAuthInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String token = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));
        if (token == null || !isValidToken(token)) {
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid or missing token"), headers);
            return new ServerCall.Listener<ReqT>() {};
        }

        return next.startCall(call, headers);
    }

    private boolean isValidToken(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256(getPublicKey(), null);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(KEYCLOAK_URL).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public RSAPublicKey getPublicKey() {
        try {
            // Richiama l'endpoint per ottenere le chiavi pubbliche del realm
            URL url = new URL(TOKEN_CERT_URL);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(url);

            // Estrai il nodo che contiene le chiavi pubbliche
            JsonNode keys = root.get("keys");

            // Seleziona la chiave giusta (generalmente la prima)
            JsonNode key = keys.get(0);

            // Ottieni il modulo "n" e l'esponente "e"
            String n = key.get("n").asText();
            String e = key.get("e").asText();

            // Decodifica il modulo e l'esponente da base64url
            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
            BigInteger publicExponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

            // Crea una specifica per la chiave pubblica RSA
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Genera e ritorna la chiave pubblica
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            throw new ConcurrentModificationException("Errore nella conversione della chiave pubblica", ex);
        }
    }
}

