package com.assignment.spring.api;

import static com.assignment.spring.dto.Unit.METRIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpMethod.GET;

import com.assignment.spring.Application;
import com.assignment.spring.dto.ApiError;
import com.assignment.spring.dto.WeatherDto;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;
import com.assignment.spring.weather.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerTest {

    //this overrides the restTemplate on WeatherServiceImpl
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherRepository weatherRepository;

    private RestTemplate testTemplate = new RestTemplate();

    public static final ParameterizedTypeReference<List<WeatherDto>> LIST_TYPE = new ParameterizedTypeReference<List<WeatherDto>>() {
    };

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        weatherRepository.deleteAll();
    }


    @Test
    public void testGetCity() throws IOException {

        final WeatherResponse weatherResponse = objectMapper.readValue(new File("src/test/resources/sample_response.json"),
            WeatherResponse.class);

        final ResponseEntity<WeatherResponse> responseEntity = ResponseEntity.ok(weatherResponse);

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(WeatherResponse.class), Mockito.anyMap()))
            .thenReturn(responseEntity);

        ResponseEntity<WeatherDto> response = testTemplate
            .getForEntity("http://localhost:" + port + "/weather/Amsterdam", WeatherDto.class);

        final WeatherDto body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Amsterdam", body.getCity());
        assertEquals("NL", body.getCountry());
        assertEquals(METRIC.toString(), body.getUnit());
        assertEquals(16.66, body.getTemperature());

        final List<WeatherEntity> all = weatherRepository.findByCity("Amsterdam");
        final WeatherEntity weatherEntity = all.get(0);

        assertEquals(1, all.size());
        assertEquals("Amsterdam", weatherEntity.getCity());
        assertEquals("NL", weatherEntity.getCountry());
        assertEquals(METRIC.toString(), weatherEntity.getUnit());
        assertEquals(16.66, weatherEntity.getTemperature());

    }

    @Test
    public void testGetCityRestTemplateError() throws IOException {
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(WeatherResponse.class), Mockito.anyMap()))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND,
                "city not found",
                "{\"cod\":\"404\",\"message\":\"city not found\"}".getBytes(),
                Charset.defaultCharset()));


        final HttpServerErrorException exception = assertThrows(HttpServerErrorException.class,
            () -> testTemplate
                .getForEntity("http://localhost:" + port + "/weather/Amsterdam", String.class));

        final ApiError apiError = objectMapper.readValue(exception.getResponseBodyAsString(), ApiError.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiError.getStatus());
        assertEquals("city not found", apiError.getMessage());
        assertNotNull(apiError.getTimestamp());
    }

    @Test
    public void testGetCityAll() {

        final WeatherEntity weatherEntity1 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("NL")
            .unit(METRIC.toString())
            .temperature(13.1)
            .build();
        weatherRepository.save(weatherEntity1);

        final WeatherEntity weatherEntity2 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("NL")
            .unit(METRIC.toString())
            .temperature(14.1)
            .build();
        weatherRepository.save(weatherEntity2);

        final WeatherEntity weatherEntity3 = WeatherEntity.builder()
            .city("Utrecht")
            .country("NL")
            .unit(METRIC.toString())
            .temperature(15.1)
            .build();
        weatherRepository.save(weatherEntity3);

        final ResponseEntity<List<WeatherDto>> response = testTemplate
            .exchange("http://localhost:" + port + "/weather/Amsterdam/all",
                GET, RequestEntity.EMPTY, LIST_TYPE);


        final List<WeatherDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, body.size());
    }

    @Test
    public void testGetCityMostRecent() {

        final WeatherEntity weatherEntity1 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("NL")
            .unit(METRIC.toString())
            .temperature(13.1)
            .build();
        weatherRepository.save(weatherEntity1);

        final WeatherEntity weatherEntity2 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("NL")
            .unit(METRIC.toString())
            .temperature(14.1)
            .build();
        weatherRepository.save(weatherEntity2);

        final WeatherEntity weatherEntity3 = WeatherEntity.builder()
            .city("Utrecht")
            .country("NL")
            .unit(METRIC.toString())
            .temperature(15.1)
            .build();
        weatherRepository.save(weatherEntity3);

        final ResponseEntity<List<WeatherDto>> response = testTemplate
            .exchange("http://localhost:" + port + "/weather/Amsterdam/recent",
                GET, RequestEntity.EMPTY, LIST_TYPE);


        final List<WeatherDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, body.size());
        assertEquals(14.1, body.get(0).getTemperature());
    }

    @Test
    public void testGetCityAllNoContent() {

        final ResponseEntity<List<WeatherDto>> response = testTemplate
            .exchange("http://localhost:" + port + "/weather/Amsterdam/all",
                GET, RequestEntity.EMPTY, LIST_TYPE);


        final List<WeatherDto> body = response.getBody();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(body);

    }
}
