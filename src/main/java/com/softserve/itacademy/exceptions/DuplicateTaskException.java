package com.softserve.itacademy.exceptions;

public class DuplicateTaskException extends RuntimeException{
    public DuplicateTaskException(String message) {
        super(message);
    }
}
