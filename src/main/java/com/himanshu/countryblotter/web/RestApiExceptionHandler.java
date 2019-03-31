package com.himanshu.countryblotter.web;

import com.himanshu.countryblotter.domain.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RestApiExceptionHandler {
  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorDetails captureException(Exception e, HttpServletResponse response, HttpServletRequest request) {
    return new ErrorDetails(e.getMessage(), e.getStackTrace());
  }
}
