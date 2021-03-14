package com.music.exceptions;

public class GeneralResponseException extends RuntimeException{

    private final String message;

    public GeneralResponseException(Exception ex, String message) {
        super(ex);
        this.message = message;
    }

    public GeneralResponseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
