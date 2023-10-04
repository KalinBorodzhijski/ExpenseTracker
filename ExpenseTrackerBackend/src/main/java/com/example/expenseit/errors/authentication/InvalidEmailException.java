package com.example.expenseit.errors.authentication;

public class InvalidEmailException extends AuthException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
