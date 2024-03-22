package pl.amilosh.managementservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.amilosh.managementservice.exception.ConversionException;
import pl.amilosh.managementservice.exception.ResourceNotFoundException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return handleExceptionInternal(ex, ErrorCode.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<Object> handleConversionException(ConversionException ex) {
        return handleExceptionInternal(ex, ErrorCode.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return handleExceptionInternal(ex, ErrorCode.NOT_FOUND, NOT_FOUND);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, String errorCode, HttpStatus status) {
        log.error(errorCode, ex);
        var errorResponse = ErrorResponse.builder()
            .errorMessage(ex.getMessage())
            .errorCode(errorCode)
            .build();
        return new ResponseEntity<>(errorResponse, status);
    }
}
