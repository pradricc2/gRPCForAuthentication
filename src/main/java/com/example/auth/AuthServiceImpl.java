package com.example.auth;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.dao.UserDAO;
import com.example.util.JwtUtil;
import io.grpc.stub.StreamObserver;
import com.example.auth.AuthServiceProto.*;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void registerUser(User request, StreamObserver<AuthResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        AuthResponse.Builder response = AuthResponse.newBuilder();

        // Hash della password usando BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        boolean success = userDAO.registerUser(username, hashedPassword);

        if (success) {
            response.setSuccess(true)
                    .setMessage("User registered successfully");
        } else {
            response.setSuccess(false)
                    .setMessage("User already exists");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void login(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        AuthResponse.Builder response = AuthResponse.newBuilder();

        boolean authenticated = userDAO.authenticateUser(username, password);

        if (authenticated) {
            // Genera un token JWT e lo restituisce come parte della risposta
            String token = JwtUtil.generateToken(username);
            response.setSuccess(true)
                    .setMessage("Login successful")
                    .setToken(token);  // Aggiungi il token alla risposta
        } else {
            response.setSuccess(false)
                    .setMessage("Invalid username or password");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void validateToken(TokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        String token = request.getToken();

        TokenResponse.Builder response = TokenResponse.newBuilder();
        try {
            // Validazione del token
            String username = JwtUtil.validateToken(token);

            if (username != null) {
                response.setValid(true)
                        .setUsername(username);
            } else {
                response.setValid(false);
            }
        } catch (TokenExpiredException e) {
            response.setValid(false).setMessage("Token expired");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
