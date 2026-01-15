package com.example.basicauth.exception;

public class NotValidException extends RuntimeException {
  public NotValidException(String message) {
    super(message);
  }
}
