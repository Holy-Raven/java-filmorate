package ru.yandex.practicum.filmorate.exception;

public class MyValidationException extends RuntimeException{

    public MyValidationException(String message) {
        super(message);
    }
}