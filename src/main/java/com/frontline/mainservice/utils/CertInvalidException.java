package com.frontline.mainservice.utils;

public class CertInvalidException extends Exception {
    public CertInvalidException(String errorMessage){
        super(errorMessage);
    }
}
