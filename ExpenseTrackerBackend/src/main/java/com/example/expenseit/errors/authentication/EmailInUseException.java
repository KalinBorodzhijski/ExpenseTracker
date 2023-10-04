package com.example.expenseit.errors.authentication;

public class EmailInUseException extends AuthException{
    public EmailInUseException(String message) {
        super(message);
    }
}
