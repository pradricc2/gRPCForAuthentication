package com.example.client;

import com.example.auth.AuthServiceGrpc;
import com.example.auth.AuthServiceProto.*;
import com.example.util.LoggerUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class AuthClient {

    private static final String PASSWORD = "password123";

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        // Register a new user
        User newUser = User.newBuilder().setUsername("john").setPassword(PASSWORD).build();
        AuthResponse response = stub.registerUser(newUser);
        LoggerUtil.info(response.getMessage());

        //Register same user
        User sameUser = User.newBuilder().setUsername("john").setPassword(PASSWORD).build();
        AuthResponse anotherResponse = stub.registerUser(sameUser);
        LoggerUtil.info(anotherResponse.getMessage());

        // Attempt login
        AuthRequest loginRequest = AuthRequest.newBuilder().setUsername("john").setPassword(PASSWORD).build();
        AuthResponse loginResponse = stub.login(loginRequest);
        LoggerUtil.info(loginResponse.getMessage());

        // Attempt login with wrong password
        AuthRequest loginRequestError = AuthRequest.newBuilder().setUsername("john").setPassword("123").build();
        AuthResponse loginResponseError = stub.login(loginRequestError);
        LoggerUtil.info(loginResponseError.getMessage());

        channel.shutdown();
    }
}

