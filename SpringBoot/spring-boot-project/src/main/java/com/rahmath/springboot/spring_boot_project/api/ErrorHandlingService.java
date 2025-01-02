package com.rahmath.springboot.spring_boot_project.api;

import com.rahmath.springboot.spring_boot_project.exception.ApplicationError;
import com.rahmath.springboot.spring_boot_project.exception.IdNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorHandlingService extends ResponseEntityExceptionHandler {

    @Value("${api_doc_url}")
    private String details;

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ApplicationError> handleEmployeeNotFoundException(
            IdNotFoundException exception, WebRequest request
    ) {
        ApplicationError error = new ApplicationError();
        error.setCode(101);
        error.setMessage("Provided ID not found.");
        error.setDetails(details);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
