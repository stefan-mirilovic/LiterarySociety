package com.paymentconcentrator.bank.exception;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
