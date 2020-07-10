package com.assignment.spring.converter;

import static com.assignment.spring.dto.Unit.METRIC;

import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.weather.WeatherResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeatherResponseConverter implements Converter<WeatherResponse, WeatherEntity> {
    @Override
    public WeatherEntity convert(final WeatherResponse weatherResponse) {
        return WeatherEntity.builder()
            .country(weatherResponse.getSys().getCountry())
            .city(weatherResponse.getName())
            .temperature(weatherResponse.getMain().getTemp())
            .unit(METRIC.toString())//if openweather will be called with parameterized unit, this should be parameterized.
            .build();
    }
}
