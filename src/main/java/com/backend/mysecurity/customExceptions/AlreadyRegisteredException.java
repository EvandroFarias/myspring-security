package com.backend.mysecurity.customExceptions;

public class AlreadyRegisteredException extends Exception{
    public AlreadyRegisteredException(String message) {
        super(message);
    }
}
