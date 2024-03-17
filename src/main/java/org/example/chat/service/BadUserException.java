package org.example.chat.service;

public class BadUserException extends Exception {

    public BadUserException(String message) {
        super(message);
    }
}
