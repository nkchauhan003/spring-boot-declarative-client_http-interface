package com.cb.exception;

public class ExternalCommunicationException extends Exception {
    public ExternalCommunicationException(int value) {
        super("Code: " + value);
    }
}
