package com.example.basicauth.exception;

public class UserBlockedException extends RuntimeException {
  public UserBlockedException(String message) {
    super(message);
  }
}
