package com.example.client;

import com.example.auth.AuthServiceGrpc;
import com.example.auth.AuthServiceProto.*;
import com.example.exception.TokenWriteException;
import com.example.util.ConfigUtil;
import com.example.util.LoggerUtil;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.util.Constants.*;
import static com.example.util.TokenRefresher.*;


public class AuthClient {


    public static void main(String[] args) throws TokenWriteException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        // Aggiungi il token JWT nell'intestazione
        String refreshToken = getRefreshToken();
        String accessToken = useRefreshToken(refreshToken);
        LoggerUtil.info("Access token: " + accessToken);
        LoggerUtil.info("Refresh token: " + refreshToken);

        // Verifica se l'access token è scaduto
        if (isTokenExpired(accessToken)) {
            // Ottieni un nuovo token con il refresh token
            LoggerUtil.info("Access token scaduto, ottenimento di un nuovo token...");
            accessToken = useRefreshToken(refreshToken);
        }

        Metadata headers = new Metadata();
        headers.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), accessToken);

        // Utilizzare un'intercettore per aggiungere i metadati
        ClientInterceptor headerInterceptor = MetadataUtils.newAttachHeadersInterceptor(headers);
        stub = stub.withInterceptors(headerInterceptor);

        // Register a new user
        User newUser = User.newBuilder().setUsername("jim").setPassword(PASSWORD).build();
        AuthResponse response = stub.registerUser(newUser);
        LoggerUtil.info(response.getMessage());


        // Attempt login
        AuthRequest loginRequest = AuthRequest.newBuilder().setUsername("jim").setPassword(PASSWORD).build();
        AuthResponse loginResponse = stub.login(loginRequest);
        LoggerUtil.info(loginResponse.getMessage());


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigUtil.getJwtFile()))) {
            writer.write(accessToken);
        } catch (IOException e) {
            throw new TokenWriteException("Failed to write token from file", e);
        }


        // Attempt login with wrong password
        AuthRequest loginRequestError = AuthRequest.newBuilder().setUsername("john").setPassword("123").build();
        AuthResponse loginResponseError = stub.login(loginRequestError);
        LoggerUtil.info(loginResponseError.getMessage());

        channel.shutdown();
    }
}

