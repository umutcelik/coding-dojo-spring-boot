package com.assignment.spring.converter;

import static com.assignment.spring.dto.Unit.METRIC;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assignment.spring.dto.WeatherDto;
import com.assignment.spring.entity.WeatherEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherEntityConverterTest {

    private WeatherEntityConverter weatherEntityConverter;

    @BeforeEach
    public void setUp() {
        weatherEntityConverter = new WeatherEntityConverter();
    }

    @Test
    public void testConvert() {

        final WeatherEntity weatherEntity = WeatherEntity.builder()
            .id(3)
            .country("NL")
            .city("Amsterdam")
            .unit(METRIC.toString())
            .temperature(14.3)
            .build();

        final WeatherDto actual = weatherEntityConverter.convert(weatherEntity);

        assertEquals(weatherEntity.getCountry(), actual.getCountry());
        assertEquals(weatherEntity.getCity(), actual.getCity());
        assertEquals(weatherEntity.getUnit(), actual.getUnit());
        assertEquals(weatherEntity.getTemperature(), actual.getTemperature());
    }
}
