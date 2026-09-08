package com.pablo.notification.notification.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String externalId) {
        super("User não encontrado: " + externalId);
    }
}