package com.cbfacademy.apiassessment.exceptions;


import com.cbfacademy.apiassessment.exceptions.customException.UserAlreadyExists;
import com.cbfacademy.apiassessment.exceptions.customException.UserNotFoundException;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();

        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.stream().map(objectError -> validationErrors.put(((FieldError) objectError).getField(), objectError.getDefaultMessage())).collect(Collectors.toList());

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(UserFilterException.class)
//    public ResponseEntity<ErrorDetails> handleUserFilterException(Exception exception, WebRequest webRequest) {
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(mapToErrorDetails(exception, "BAD_REQUEST", webRequest));
//    }
//
//    @ExceptionHandler(QuestionNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleQuestionNotFoundException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(mapToErrorDetails(exception, "NOT_FOUND", webRequest));
//    }

//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ErrorDetails> handleBadRequestException(Exception exception, WebRequest webRequest) {
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(mapToErrorDetails(exception, "BAD_REQUEST", webRequest));
//    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(mapToErrorDetails(exception, "RESOURCE_NOT_FOUND", webRequest));
//    }
//
//    @ExceptionHandler(AssessmentNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleAssessmentNotFoundException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(mapToErrorDetails(exception, "ASSESSMENT_NOT_FOUND", webRequest));
//    }
//
//    @ExceptionHandler(ForbiddenException.class)
//    public ResponseEntity<ErrorDetails> handleForbiddenException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body(mapToErrorDetails(exception, "FORBIDDEN", webRequest));
//    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapToErrorDetails(exception, "USER_NOT_FOUND", webRequest));
    }

//    @ExceptionHandler(OrganizationNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleOrganizationNotFoundException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(mapToErrorDetails(exception, "ORGANIZATION_NOT_FOUND", webRequest));
//    }
//
//    @ExceptionHandler(UnauthorizedException.class)
//    public ResponseEntity<ErrorDetails> handleUnauthorizedException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(mapToErrorDetails(exception, "UNAUTHORIZED", webRequest));
//    }
//
//    @ExceptionHandler(EmailAlreadyExistsException.class)
//    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(mapToErrorDetails(exception, "CONFLICT", webRequest));
//    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(Exception exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mapToErrorDetails(exception, "CONFLICT", webRequest));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapToErrorDetails(exception, "INTERNAL_SERVER_ERROR", webRequest));
    }

//    @ExceptionHandler(PermissionNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handlePermissionNotFoundException(Exception exception, WebRequest webRequest) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(mapToErrorDetails(exception, "PERMISSION_NOT_FOUND", webRequest));
//    }


    private ErrorDetails mapToErrorDetails(Exception exception, String errorCodeMessage, WebRequest webRequest) {
        return ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(webRequest.getDescription(false))
                .errorCode(errorCodeMessage)
                .build();
    }
}