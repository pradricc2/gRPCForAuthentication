package com.example.client;

import com.example.auth.AuthServiceGrpc;
import com.example.auth.AuthServiceProto.*;
import com.example.exception.TokenReadException;
import com.example.util.JwtUtil;
import com.example.util.LoggerUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class AnotherAuthClient {

    public static void main(String[] args) throws TokenReadException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        File tokenFile = new File("jwt_token.txt");
        if (!tokenFile.exists()) {
            LoggerUtil.error("Il file jwt_token.txt non esiste. Esegui prima AuthClient.", null);
            return;
        }
        // Attempt login
        String token;
        try (BufferedReader reader = new BufferedReader(new FileReader("jwt_token.txt"))) {
            token = reader.readLine();
        } catch (IOException e) {
            throw new TokenReadException("Failed to read token from file", e);
        }

        // Calcola il tempo rimanente fino alla scadenza
        long timeUntilExpiration = JwtUtil.getTimeUntilExpiration(token);

        if (timeUntilExpiration > 0) {
            LoggerUtil.info("Tempo rimanente fino alla scadenza del token: " + (timeUntilExpiration / 1000) + " secondi");
        } else {
            LoggerUtil.info("Il token è già scaduto o non è valido");
        }

        //autenticazione token
        TokenRequest tokenRequest = TokenRequest.newBuilder().setToken(token).build();
        TokenResponse tokenResponse = stub.validateToken(tokenRequest);
        if (tokenResponse.getValid()) {
            LoggerUtil.info("Token valido. Username: " + tokenResponse.getUsername());
        } else {
            LoggerUtil.info("Token non valido");
        }

        channel.shutdown();
    }
}

