package com.example.auth;

import io.grpc.stub.StreamObserver;
import com.example.auth.AuthServiceProto.*;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final Map<String, String> users = new HashMap<>();

    @Override
    public void registerUser(User request, StreamObserver<AuthResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        AuthResponse.Builder response = AuthResponse.newBuilder();

        if (users.containsKey(username)) {
            response.setSuccess(false)
                    .setMessage("User already exists");
        } else {
            users.put(username, password);
            response.setSuccess(true)
                    .setMessage("User registered successfully");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void login(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        AuthResponse.Builder response = AuthResponse.newBuilder();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            response.setSuccess(true)
                    .setMessage("Login successful");
        } else {
            response.setSuccess(false)
                    .setMessage("Invalid username or password");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
