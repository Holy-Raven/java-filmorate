package ru.yandex.practicum.filmorate.exception;

public class DataBaseNotFoundException extends RuntimeException {
    public DataBaseNotFoundException(String message) {
        super(message);
    }
}
