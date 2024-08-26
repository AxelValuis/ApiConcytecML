package org.concytec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Resource not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericClientException.class)
    public ResponseEntity<ErrorDTO> handleGenericClientException(GenericClientException exception, HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorDTO dto = ErrorDTO.from(exception, path);
        return ResponseEntity.status(dto.getHttpStatus()).body(dto);
    }

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<ErrorDTO> handleNumberFormatException(NumberFormatException ex, HttpServletRequest request){
        ErrorDTO dto = new ErrorDTO(
                LocalDateTime.now(),
                "INVALID_INPUT",
                "The provided input cannot be converted to the required type: "+ ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("The parameter '%s' with value '%s' could not be converted to type '%s'",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType().getSimpleName());
        ErrorDTO dto = new ErrorDTO(
                LocalDateTime.now(),
                "TYPE_MISMATCH",
                message,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleAllExceptions(Exception ex, HttpServletRequest request) {
        ErrorDTO dto = new ErrorDTO(
                LocalDateTime.now(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

}
