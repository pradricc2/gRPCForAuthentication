package com.example.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;

import static com.example.util.Constants.*;

public class TokenRefresher {

    private TokenRefresher() {
    }

    public static String useRefreshToken(String refreshToken) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", REFRESH_TOKEN_STRING)
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("refresh_token", refreshToken)
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            LoggerUtil.info("Response code: " + response.code());
            LoggerUtil.info("Response message: " + response.message());

            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                LoggerUtil.info("Response body: " + responseBody);

                // Parse the JSON to get the new access_token
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.get("access_token").asText();
            } else {
                LoggerUtil.info("Failed to refresh token: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            LoggerUtil.error("Exception in refresh token process", e);
        }

        return null;
    }

    public static String getRefreshToken() {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("username", "test")
                .add("password", "test")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                // Parse the JSON to get the access_token
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.get("refresh_token").asText();
            } else {
                LoggerUtil.info("Request to Keycloak failed: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Metodo per verificare se il token è scaduto
    public static boolean isTokenExpired(String token) {
        try {
            // Rimuovi "Bearer " dal token, se presente
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Decodifica il token senza verificare la firma
            DecodedJWT decodedJWT = JWT.decode(token);

            // Ottieni il claim 'exp' (expiration time)
            Date expiration = decodedJWT.getExpiresAt();

            // Confronta l'ora corrente con la scadenza
            return (expiration != null && expiration.before(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
            return true; // In caso di errore, considera il token come non valido/scaduto
        }
    }
}

