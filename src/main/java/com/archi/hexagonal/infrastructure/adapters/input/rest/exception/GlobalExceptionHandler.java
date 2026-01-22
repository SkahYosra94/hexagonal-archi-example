package com.archi.hexagonal.infrastructure.adapters.input.rest.exception;

import com.archi.hexagonal.domain.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

//Intercepte toutes les exceptions levées par les controllers REST, pour centraliser la gestion des erreurs
//@RestControllerAdvice = @ControllerAdvice + @ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {
    //On loggue toujours l’erreur côté serveur, mais on expose un message générique au client.

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // Gestion validation des entrées
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    // Gestion ressource non trouvée
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage(),
                "PRODUCT_NOT_FOUND",
                Instant.now()
        ));
    }

    // Gestion générique
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOtherErrors(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(ex.getMessage(), "INTERNAL_SERVER_ERROR", Instant.now()));
    }
}
