package com.example.expenseit.errors.authentication;

public class JwtTokenExpiredException extends AuthException{
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}
