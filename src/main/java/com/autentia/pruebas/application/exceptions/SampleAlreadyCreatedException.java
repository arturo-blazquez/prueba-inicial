package com.autentia.pruebas.application.exceptions;

public class SampleAlreadyCreatedException extends Exception {
    private static final long serialVersionUID = 1L;
    public static final String ERROR_MESSAGE = "Sample ya en la base de datos";

    public SampleAlreadyCreatedException() {
        super(ERROR_MESSAGE);
    }
}