package com.koreshkov.tinkoffservice.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException (String message) {super(message);}
}
