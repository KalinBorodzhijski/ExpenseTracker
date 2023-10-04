package com.example.expenseit.errors.authentication;

public class NoJwtTokenProvidedException extends AuthException {
    public NoJwtTokenProvidedException(String message) {
        super(message);
    }
}