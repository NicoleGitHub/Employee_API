package com.example.restapi.advise;

import com.example.restapi.exception.NoCompaniesFoundException;
import com.example.restapi.exception.NoEmployeesFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GlobalControllerAdvice {

    @ExceptionHandler({NoEmployeesFoundException.class, NoCompaniesFoundException.class})
    public ErrorResponse handleNotFound(Exception exception) {
        return new ErrorResponse(404, "Entity Not Found.");
    }
}
