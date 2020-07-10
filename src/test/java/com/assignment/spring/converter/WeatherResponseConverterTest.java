package com.assignment.spring.converter;

import static com.assignment.spring.dto.Unit.METRIC;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.weather.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherResponseConverterTest {

    private WeatherResponseConverter weatherResponseConverter;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        weatherResponseConverter = new WeatherResponseConverter();
    }

    @Test
    public void testConvert() throws IOException {
        final WeatherResponse weatherResponse = OBJECT_MAPPER.readValue(new File("src/test/resources/sample_response.json"),
            WeatherResponse.class);
        final WeatherEntity actual = weatherResponseConverter.convert(weatherResponse);
        assertEquals(weatherResponse.getSys().getCountry(), actual.getCountry());
        assertEquals(weatherResponse.getName(), actual.getCity());
        assertEquals(weatherResponse.getMain().getTemp(), actual.getTemperature());
        assertEquals(METRIC.toString(), actual.getUnit());
    }
}
