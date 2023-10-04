package com.example.expenseit.errors.authentication;

public class LoginFailedException extends AuthException {
    public LoginFailedException(String message) {
        super(message);
    }
}
