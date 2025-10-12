package com.rentmate.service.item.exception;

public class OwnerMismatchException extends RuntimeException {
    public OwnerMismatchException() {
        super("Unauthorized: You do not own this item");
    }
}
