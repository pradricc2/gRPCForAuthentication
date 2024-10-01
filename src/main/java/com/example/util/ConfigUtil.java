package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private ConfigUtil() {
    }

    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                LoggerUtil.info("Sorry, unable to find application.properties");
                throw new IOException("Unable to find application.properties");
            } else {
                // Carica le proprietà dal file config.properties
                properties.load(input);
            }
        } catch (IOException ex) {
            LoggerUtil.error("application.properties error ", ex);
            throw new ExceptionInInitializerError("Failed to load configuration file");
        }
    }

    // Metodo per ottenere il valore della chiave jwt.secret
    public static String getJwtSecret() {
        return properties.getProperty("jwt.secret");
    }

    // Metodo per ottenere il valore della durata del token jwt.expiration
    public static long getJwtExpiration() {
        return Long.parseLong(properties.getProperty("jwt.expiration"));
    }

    public static String getPassword() {
        return properties.getProperty("jwt.password");
    }
}
