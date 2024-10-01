package com.example.exception;

import java.io.IOException;

public class TokenWriteException extends IOException {
    public TokenWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
