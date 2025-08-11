package com.kozitskiy.dorm4.sys.exceptions;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
