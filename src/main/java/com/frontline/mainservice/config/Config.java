package com.frontline.mainservice.config;

public class Config {

    public static String keyStoreName;
    public static String keyStorePassword;
    public static final String SOURCE_PATH = "src/main/resources/";
    public static final String LUTHER_SERVICE_URL = "http://52.45.29.135:3000/api/eval";
    public static final String ALIAS_NAME = "ms-service-service1.org";

    public static final String GET_ALL_CATALOGUE_ITEMS_URL = "https://localhost:8085/api/item";
    public static final String GET_ONE_CATALOGUE_ITEMS_URL = "https://localhost:8085/api/item/";
    public static final String POST_CATALOGUE_ITEM_URL = "https://localhost:8085/api/item";


    public static final String REQUEST_SUCCEED_MSG = "Request Successfully completed";
    public static final String REQUEST_FAILED_MSG = "Request Failed";
    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;


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
