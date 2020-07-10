package com.assignment.spring.api;

import com.assignment.spring.dto.WeatherDto;
import com.assignment.spring.service.WeatherService;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherController implements WeatherApi {

    private final WeatherService weatherServiceImpl;

    @Override
    public WeatherDto getWeatherByCity(@NotBlank String city) {

        return weatherServiceImpl.getByCity(city);
    }

    @Override
    public List<WeatherDto> getWeatherByCityAll(@NotBlank String city) {
        return weatherServiceImpl.getByCityAll(city);
    }

    @Override
    public List<WeatherDto> getWeatherByCityMostRecent(@NotBlank String city) {
        return weatherServiceImpl.getByCityMostRecent(city);
    }
}
