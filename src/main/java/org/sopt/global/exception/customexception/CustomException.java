package org.sopt.global.exception.customexception;

public abstract class CustomException extends RuntimeException {

    private final String message;

    public CustomException(String message) {
        this.message = message;
    }
}
