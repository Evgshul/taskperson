package com.evgshul.taskperson.exception;

import com.evgshul.taskperson.model.Logg;
import com.evgshul.taskperson.service.LoggService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private LoggService loggService;
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> messages = result.getFieldErrors();
        String defaultMessage =
                messages.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.toString(), defaultMessage);
        log.error("Entity validation fail: Date: {}, message: {}", errorDetails.getTimestamp().toString(), defaultMessage);
        loggService.saveLogg(new Logg(errorDetails.getTimestamp(), log.getName(), Level.ERROR.toString(), defaultMessage));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
