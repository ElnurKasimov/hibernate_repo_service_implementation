package com.softserve.itacademy.exceptions;

public class ToDoNotFoundException extends RuntimeException{
    public ToDoNotFoundException(String message) {
        super(message);
    }
}
