package ru.bootjava.vote.error;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg);
    }
}