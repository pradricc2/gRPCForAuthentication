package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private ConfigUtil() {
    }

    private static final Properties properties = new Properties();

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


    public static String getPassword() {
        return properties.getProperty("jwt.password");
    }

    public static String getClientId() { return properties.getProperty("keycloak.resource"); }

    public static String getClientSecret() { return properties.getProperty("keycloak.credentials.secret"); }

    public static String getKeycloakUrl() { return properties.getProperty("keycloak.auth-server-url"); }

    public static String getPort() { return properties.getProperty("server.port"); }

    public static String getJwtFile() { return properties.getProperty("jwt.file"); }
}
