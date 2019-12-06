package com.frontline.mainservice.config;

public class Config {

    public static String keyStoreName;
    public static String keyStorePassword;
    public static final String SOURCE_PATH = "src/main/resources";
    public static final String LUTHER_SERVICE_URL = "http://52.45.29.135:3000/api/eval";

    public static String getKeyStoreName() {
        return keyStoreName;
    }

    public static void setKeyStoreName(String keyStoreName) {
        Config.keyStoreName = keyStoreName;
    }

    public static String getKeyStorePassword() {
        return keyStorePassword;
    }

    public static void setKeyStorePassword(String keyStorePassword) {
        Config.keyStorePassword = keyStorePassword;
    }
}
