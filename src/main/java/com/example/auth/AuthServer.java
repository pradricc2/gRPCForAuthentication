package com.example.auth;

import com.example.util.HibernateUtil;
import com.example.util.LoggerUtil;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class AuthServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new AuthServiceImpl())
                .build();

        LoggerUtil.info("Starting server...");
        server.start();
        LoggerUtil.info("Server started on port 8080");

        // Aggiungi un hook per chiudere Hibernate quando il server viene interrotto
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtil.info("Shutting down server...");
            server.shutdown();
            HibernateUtil.shutdown();
            LoggerUtil.info("Server shut down.");
        }));

        server.awaitTermination();
    }
}

