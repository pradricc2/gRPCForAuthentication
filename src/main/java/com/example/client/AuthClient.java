package com.example.client;

import com.example.auth.AuthServiceGrpc;
import com.example.auth.AuthServiceProto.*;
import com.example.exception.TokenWriteException;
import com.example.util.ConfigUtil;
import com.example.util.LoggerUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class AuthClient {

    private static final String PASSWORD = ConfigUtil.getPassword();

    public static void main(String[] args) throws TokenWriteException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        // Register a new user
        User newUser = User.newBuilder().setUsername("john").setPassword(PASSWORD).build();
        AuthResponse response = stub.registerUser(newUser);
        LoggerUtil.info(response.getMessage());


        // Attempt login
        AuthRequest loginRequest = AuthRequest.newBuilder().setUsername("john").setPassword(PASSWORD).build();
        AuthResponse loginResponse = stub.login(loginRequest);
        LoggerUtil.info(loginResponse.getMessage());
        String token = loginResponse.getToken();
        LoggerUtil.info("Token JWT: " + token);

        //autenticazione token
        TokenRequest tokenRequest = TokenRequest.newBuilder().setToken(token).build();
        TokenResponse tokenResponse = stub.validateToken(tokenRequest);
        if (tokenResponse.getValid()) {
            LoggerUtil.info("Token valido. Username: " + tokenResponse.getUsername());
        } else {
            LoggerUtil.info("Token non valido");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("jwt_token.txt"))) {
            writer.write(token);
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

