package com.readflow.api.exception.global;

import java.lang.reflect.Field;

import com.readflow.api.entity.Book;
import com.readflow.api.exception.global.exceptions.BookNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidationErrors (MethodArgumentNotValidException ex){

            List<String> fieldOrder = Arrays.stream(Book.class.getDeclaredFields())
                    .map(Field::getName)
                    .toList();

            Map<String, String> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .sorted(Comparator.comparingInt(error -> {
                        int index = fieldOrder.indexOf(error.getField());
                        return index == -1 ? Integer.MAX_VALUE : index;
                    }))
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage(),
                            (existing, replacement) -> existing,
                            LinkedHashMap::new
                    ));

            var body = new ErrorResponse(
                    400,
                    "Bad Request",
                    errors
            );

            return ResponseEntity.badRequest().body(body);
        }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DataIntegrityViolationException ex) {

        ErrorResponse error = new ErrorResponse(
                409,
                "A book with this title and author already exists",
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
