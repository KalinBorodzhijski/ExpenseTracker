package com.example.expenseit.errors;

import com.example.expenseit.errors.authentication.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException(AuthException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        if (e instanceof LoginFailedException) {
            status = HttpStatus.FORBIDDEN;
        } else if (e instanceof InvalidEmailException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof EmailInUseException) {
            status = HttpStatus.CONFLICT;
        } else if (e instanceof JwtTokenExpiredException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (e instanceof NoJwtTokenProvidedException) {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(e.getMessage(), status);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException(InvalidDataException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
