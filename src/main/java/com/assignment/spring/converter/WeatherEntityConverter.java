package com.assignment.spring.converter;

import com.assignment.spring.dto.WeatherDto;
import com.assignment.spring.entity.WeatherEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WeatherEntityConverter implements Converter<WeatherEntity, WeatherDto> {
    @Override
    public WeatherDto convert(final WeatherEntity weatherEntity) {
        return WeatherDto.builder()
            .country(weatherEntity.getCountry())
            .city(weatherEntity.getCity())
            .unit(weatherEntity.getUnit())
            .temperature(weatherEntity.getTemperature())
            .build();
    }
}
