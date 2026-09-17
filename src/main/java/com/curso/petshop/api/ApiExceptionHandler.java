package com.curso.petshop.api;

import com.curso.petshop.application.PetshopService.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    public record ApiError(Instant timestamp, int status, String message, String path, Map<String,String> fields) {}
    private ResponseEntity<ApiError> error(HttpStatus s,String m,HttpServletRequest r,Map<String,String> f) {
        return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),m,r.getRequestURI(),f));
    }
    @ExceptionHandler(NaoEncontrado.class)
    public ResponseEntity<ApiError> naoEncontrado(NaoEncontrado e,HttpServletRequest r) { return error(HttpStatus.NOT_FOUND,e.getMessage(),r,Map.of()); }
    @ExceptionHandler({Conflito.class,DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> conflito(RuntimeException e,HttpServletRequest r) { return error(HttpStatus.CONFLICT,e instanceof Conflito?e.getMessage():"Operação viola uma regra de integridade",r,Map.of()); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(MethodArgumentNotValidException e,HttpServletRequest r) {
        Map<String,String> campos=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f->campos.putIfAbsent(f.getField(),f.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST,"Um ou mais campos são inválidos",r,campos);
    }
    @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> formato(RuntimeException e,HttpServletRequest r) { return error(HttpStatus.BAD_REQUEST,"JSON ou parâmetro inválido",r,Map.of()); }
}
