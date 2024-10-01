package com.example.exception;

import java.io.IOException;

public class TokenReadException extends IOException {
    public TokenReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
