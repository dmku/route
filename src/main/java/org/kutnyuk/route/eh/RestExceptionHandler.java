package org.kutnyuk.route.eh;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler({JsonMappingException.class})
    @ResponseBody
    ResponseEntity<ErrorResponse> handleJsonMappingException(JsonMappingException e) {
        log.error(e.getMessage(), e);
        return buildResponseEntity(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }


    @ExceptionHandler({RouteNotFoundException.class, CountryNotFoudException.class})
    @ResponseBody
    ResponseEntity<ErrorResponse> handleRouteServiceException(ResponseStatusException e) {
        log.error(e.getMessage(), e);
        return buildResponseEntity(ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

}
