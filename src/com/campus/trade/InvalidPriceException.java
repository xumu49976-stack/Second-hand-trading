package com.campus.trade;

public class InvalidPriceException extends IllegalArgumentException{
    public InvalidPriceException() {
    }

    public InvalidPriceException(String message) {
        super(message);
    }
}
