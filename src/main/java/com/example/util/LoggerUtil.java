package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    private LoggerUtil() {
        // Costruttore privato per impedire l'istanza della classe
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
