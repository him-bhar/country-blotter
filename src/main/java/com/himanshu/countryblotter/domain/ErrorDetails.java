package com.himanshu.countryblotter.domain;

public class ErrorDetails {
  private String error;
  private StackTraceElement[] stack;

  public ErrorDetails(String error, StackTraceElement[] stack) {
    this.error = error;
    this.stack = stack;
  }

  public String getError() {
    return error;
  }

  public StackTraceElement[] getStack() {
    return stack;
  }

  @Override
  public String toString() {
    return "ErrorDetails{" +
          "error='" + error + '\'' +
          ", stack=" + stack +
          '}';
  }
}
