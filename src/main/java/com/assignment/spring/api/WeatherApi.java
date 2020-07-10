package com.assignment.spring.api;

import com.assignment.spring.dto.ApiError;
import com.assignment.spring.dto.WeatherDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@Api(value = "Weather", produces = "application/json")
@Validated
public interface WeatherApi {

    @ApiOperation(value = "Get weather for the city")
    @RequestMapping(value = "/{city}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    WeatherDto getWeatherByCity(@ApiParam(required = true, example = "Amsterdam")
                                @PathVariable("city")
                                @NotBlank final String city);

    @ApiOperation(value = "List weather records for the city")
    @RequestMapping(value = "/{city}/all", method = RequestMethod.GET)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "No weather record")
    })
    List<WeatherDto> getWeatherByCityAll(@ApiParam(required = true, example = "Amsterdam")
                                         @PathVariable("city")
                                         @NotBlank final String city);

    @ApiOperation(value = "List the most recent weather record for the city")
    @RequestMapping(value = "/{city}/recent", method = RequestMethod.GET)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "No weather record")
    })
    List<WeatherDto> getWeatherByCityMostRecent(@ApiParam(required = true, example = "Amsterdam")
                                                @PathVariable("city")
                                                @NotBlank final String city);
}
