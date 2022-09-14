package com.example.dscatalog.resources.exception;

import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

//Handler é manipulador
@ControllerAdvice // essa anotation permite que a classe intercepte alguma excessão na camada de resouce
public class ResouceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardException> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
        StandardException err = new StandardException();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Resource not found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }
    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardException> dataBaseIntegrityViolation(DataBaseException e, HttpServletRequest request){
        StandardException err = new StandardException();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Dasabase Exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> argumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        err.setError("Argumento inválido !");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        for (FieldError error: e.getBindingResult().getFieldErrors()) {
            err.addError(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }
}
