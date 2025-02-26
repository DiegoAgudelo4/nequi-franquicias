package com.nequi.franquicias.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNotFoundException(RecursoNoEncontradoException ex) {
        return Mono.just(generarRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), false));
    }

    @ExceptionHandler(ValidacionException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(ValidacionException ex) {
        return Mono.just(generarRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), false));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGeneralException(Exception ex) {
        System.err.println(ex);
        return Mono.just(generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", false));
    }

    private ResponseEntity<Map<String, Object>> generarRespuesta(HttpStatus status, String mensaje, boolean resultado) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", mensaje);
        response.put("resultado", resultado);
        response.put("data", null);
        return new ResponseEntity<>(response, status);
    }
}
