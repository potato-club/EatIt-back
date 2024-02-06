package com.gamza.ItEat.error;
import com.gamza.ItEat.error.exeption.*;
import com.gamza.ItEat.jwt.JwtExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorExceptionHandler {
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final BadRequestException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({DuplicateException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final DuplicateException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final ForbiddenException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({JwtException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final JwtException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final NotFoundException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final InvalidTokenException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final UnAuthorizedException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }

}