package com.autentia.pruebas.application.exceptions;

public class SampleBadRequestException extends Exception {
    private static final long serialVersionUID = 3L;
    public static final String ERROR_MESSAGE = "Sample id no coincide";

    public SampleBadRequestException() {
        this(ERROR_MESSAGE);
    }

    public SampleBadRequestException(String errorMessage) {
        super(errorMessage);
    }
}