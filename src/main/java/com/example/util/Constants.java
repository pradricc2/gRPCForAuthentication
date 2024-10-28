package com.example.util;

public class Constants {

    private Constants() {
    }

    public static final String PASSWORD = ConfigUtil.getPassword();
    public static final String CLIENT_ID = ConfigUtil.getClientId();
    public static final String CLIENT_SECRET = ConfigUtil.getClientSecret();
    public static final String KEYCLOAK_URL = ConfigUtil.getKeycloakUrl();
    public static final String TOKEN_URL = ConfigUtil.getKeycloakUrl() + "/protocol/openid-connect/token";
    public static final String TOKEN_CERT_URL = ConfigUtil.getKeycloakUrl() + "/protocol/openid-connect/certs";
    public static final String REFRESH_TOKEN_STRING = "refresh_token";


}
