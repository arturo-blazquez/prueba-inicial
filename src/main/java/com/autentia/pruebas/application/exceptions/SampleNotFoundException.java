package com.autentia.pruebas.application.exceptions;

public class SampleNotFoundException extends Exception {
    private static final long serialVersionUID = 2L;
    public static final String ERROR_MESSAGE = "Sample no está en la base de datos";

    public SampleNotFoundException() {
        this(ERROR_MESSAGE);
    }

    public SampleNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}