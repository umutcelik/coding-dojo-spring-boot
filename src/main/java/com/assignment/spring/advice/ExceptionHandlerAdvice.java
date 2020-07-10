package com.assignment.spring.advice;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.assignment.spring.dto.ApiError;
import com.assignment.spring.weather.Error;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller advice to handle exceptions and produces {@link ApiError} regarding the exception.
 * This handler has limited implementation, should be extend for different exceptions and pretty print messages.
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    public static final String DEFAULT_REST_TEMPLATE_MESSAGE = "Rest template error cannot be read";

    private final ObjectMapper objectMapper;

    /**
     * Handles {@link RestClientResponseException} thrown by {@link org.springframework.web.client.RestTemplate},
     * decorates {@link ApiError} with catched exception message.
     *
     * @param ex      restTemplate exception
     * @param request
     * @return ResponseEntity with {@link ApiError} and {@link HttpStatus} <code>500</code>
     */
    @ExceptionHandler(value = {RestClientResponseException.class})
    protected ResponseEntity<Object> handleRestTemplateErrors(final RestClientResponseException ex, final WebRequest request) {
        log.debug("Exception handled", ex.getMessage());

        ApiError apiError;
        try {
            final Error weatherError = objectMapper.readValue(ex.getResponseBodyAsString(), Error.class);
            apiError = ApiError.builder()
                .timestamp(now())
                .status(INTERNAL_SERVER_ERROR)
                .message(weatherError.getMessage())
                .build();
        } catch (JsonProcessingException e) {
            apiError = ApiError.builder()
                .timestamp(now())
                .status(INTERNAL_SERVER_ERROR)
                .message(DEFAULT_REST_TEMPLATE_MESSAGE)
                .build();
        }

        return handleExceptionInternal(ex, apiError,
            new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles {@link ConstraintViolationException}.
     * Prints error message to body and returns BAD_REQUEST as http response code.
     *
     * @param ex
     * @param request
     * @return ResponseEntity with message printed on the body and http status BAD_REQUEST
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidation(final ConstraintViolationException ex, final WebRequest request) {
        log.debug("Exception handled", ex.getMessage());
        //better implementation needed here to extract vialations messages properly
        final Set<String> collect = ex.getConstraintViolations().stream().map(v -> v.getMessage()).collect(Collectors.toSet());

        final ApiError apiError = ApiError.builder()
            .timestamp(now())
            .status(BAD_REQUEST)
            .message(collect.toString())
            .build();
        return handleExceptionInternal(ex, apiError,
            new HttpHeaders(), BAD_REQUEST, request);
    }

    /**
     * Handles {@link MethodArgumentNotValidException}.
     * Prints {@link ApiError} to body and returns BAD_REQUEST as http response code.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return ResponseEntity with {@link ApiError} and {@link HttpStatus} <code>400</code>
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        log.debug("Exception handled", ex.getMessage());

        final ApiError apiError = ApiError.builder()
            .timestamp(now())
            .status(BAD_REQUEST)
            .message(ex.getMessage())
            .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), BAD_REQUEST, request);
    }
}
