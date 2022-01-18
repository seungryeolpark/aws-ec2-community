package com.example.demo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionAdvice {

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public ErrorResult ExceptionHandle(Exception e) {
//        return new ErrorResult("Exception", e.getMessage());
//    }
//
//    @Data
//    @AllArgsConstructor
//    private class ErrorResult {
//        private String code;
//        private String message;
//    }
}
