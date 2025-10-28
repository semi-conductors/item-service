package com.rentmate.service.item.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String name) {
        super("0000 with name: " + name);
    }
}
