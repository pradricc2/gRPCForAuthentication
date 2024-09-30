package com.example.client;

import com.example.auth.AuthServiceGrpc;
import com.example.auth.AuthServiceProto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthClient {

    static Logger logger = LoggerFactory.getLogger(AuthClient.class);

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        // Register a new user
        User newUser = User.newBuilder().setUsername("john").setPassword("password123").build();
        AuthResponse response = stub.registerUser(newUser);
        logger.info(response.getMessage());

        // Attempt login
        AuthRequest loginRequest = AuthRequest.newBuilder().setUsername("john").setPassword("password123").build();
        AuthResponse loginResponse = stub.login(loginRequest);
        logger.info(loginResponse.getMessage());

        channel.shutdown();
    }
}

