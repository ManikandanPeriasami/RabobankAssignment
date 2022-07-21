package nl.rabobank.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for account api.
 */
@RestControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> getErrorResponse() {
        return ResponseEntity.badRequest().build();
    }

}
