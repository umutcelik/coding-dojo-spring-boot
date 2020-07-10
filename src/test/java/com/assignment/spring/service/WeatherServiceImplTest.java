package com.assignment.spring.service;


import static com.assignment.spring.dto.Unit.METRIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.assignment.spring.converter.WeatherEntityConverter;
import com.assignment.spring.converter.WeatherResponseConverter;
import com.assignment.spring.dto.WeatherDto;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;
import com.assignment.spring.weather.WeatherResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    private WeatherServiceImpl weatherServiceImpl;

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherResponseConverter weatherResponseConverter;

    @Mock
    private WeatherEntityConverter weatherEntityConverter;

    @Mock
    private ResponseEntity<WeatherResponse> response;

    @Mock
    private WeatherResponse weatherResponse;

    @Mock
    private WeatherEntity weatherEntity;

    @Mock
    private WeatherDto weatherDto;

    @Mock
    private List<WeatherEntity> weatherEntities;

    private static final Properties properties = new Properties();

    private static String apiUrl;

    private static String apiId;


    @BeforeAll
    public static void loadProperties() throws IOException {
        //this is going to get the prod values for api url and id.
        properties.load(new FileReader(new File("src/main/resources/application.properties")));
        apiUrl = properties.getProperty("weather.api.url");
        apiId = properties.getProperty("weather.api.id");
    }

    @BeforeEach
    public void setUp() {
        weatherServiceImpl = new WeatherServiceImpl(weatherRepository, restTemplate, apiUrl, apiId,
            weatherResponseConverter, weatherEntityConverter);
    }

    @Test
    public void testGetByCity() {
        final Map<String, String> queryParamMap = Map.of("appid", apiId, "unit", METRIC.toString(), "city", "Amsterdam");
        when(restTemplate.getForEntity(apiUrl, WeatherResponse.class,
            queryParamMap)).thenReturn(response);
        when(response.getBody()).thenReturn(weatherResponse);
        when(weatherResponseConverter.convert(weatherResponse)).thenReturn(weatherEntity);
        when(weatherRepository.save(weatherEntity)).thenReturn(weatherEntity);
        when(weatherEntityConverter.convert(weatherEntity)).thenReturn(weatherDto);

        final WeatherDto actual = weatherServiceImpl.getByCity("Amsterdam");

        assertEquals(actual, weatherDto);

        verify(restTemplate).getForEntity(apiUrl, WeatherResponse.class,
            queryParamMap);
        verify(weatherRepository).save(weatherEntity);
    }

    @Test
    public void testRestTemplateThrowsException() {

        when(restTemplate.getForEntity(anyString(), eq(WeatherResponse.class), any(Map.class)))
            .thenThrow(new RestClientResponseException("message", 500, "text", null, null, null));

        final RestClientResponseException exception = assertThrows(RestClientResponseException.class,
            () -> weatherServiceImpl.getByCity("Amsterdam"));
        assertTrue(exception.getMessage().equals("message"));

        verify(restTemplate).getForEntity(anyString(), eq(WeatherResponse.class), any(Map.class));
        verifyNoInteractions(weatherRepository);
    }

    @Test
    public void testGetByCityAll() {
        when(weatherRepository.findByCity("Amsterdam")).thenReturn(weatherEntities);
        weatherServiceImpl.getByCityAll("Amsterdam");

        verify(weatherRepository).findByCity("Amsterdam");
    }

    @Test
    public void testGetByCityMostRecent() {
        when(weatherRepository.findTopByAndCityOrderByIdDesc("Amsterdam")).thenReturn(weatherEntities);
        weatherServiceImpl.getByCityMostRecent("Amsterdam");

        verify(weatherRepository).findTopByAndCityOrderByIdDesc("Amsterdam");
    }
}
