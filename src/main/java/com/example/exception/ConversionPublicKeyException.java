package com.example.exception;

import java.io.IOException;

public class ConversionPublicKeyException extends IOException {
    public ConversionPublicKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
