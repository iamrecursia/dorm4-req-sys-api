package com.kozitskiy.dorm4.sys.exceptions;

public class RoomNotExistingException extends RuntimeException {
    public RoomNotExistingException(String message) {
        super(message);
    }
}
