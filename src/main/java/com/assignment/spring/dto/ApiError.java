package com.assignment.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Generic api error model for the service.
 */
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    @ApiModelProperty(example = "BAD_REQUEST", notes = "Http status")
    private HttpStatus status;
    @ApiModelProperty(example = "2019-05-06T19:00:20.529", notes = "Time of error")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    @ApiModelProperty(example = "Invalid parameter", notes = "Exception message")
    private String message;

    public ApiError(final HttpStatus status, final String message) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }
}
