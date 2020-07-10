package com.assignment.spring.service;

import static com.assignment.spring.dto.Unit.METRIC;

import com.assignment.spring.converter.WeatherEntityConverter;
import com.assignment.spring.converter.WeatherResponseConverter;
import com.assignment.spring.dto.WeatherDto;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;
import com.assignment.spring.weather.WeatherResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    public static final String QUERY_PARAM_CITY = "city";
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final WeatherResponseConverter weatherResponseConverter;
    private final WeatherEntityConverter weatherEntityConverter;
    private final Map<String, String> cityQueryParam;


    public WeatherServiceImpl(WeatherRepository weatherRepository,
                              RestTemplate restTemplate,
                              @Value("${weather.api.url}") String apiUrl,
                              @Value("${weather.api.id}") String apiId,
                              WeatherResponseConverter weatherResponseConverter,
                              WeatherEntityConverter weatherEntityConverter) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.weatherResponseConverter = weatherResponseConverter;
        this.weatherEntityConverter = weatherEntityConverter;
        cityQueryParam = new HashMap<>(Map.of("appid", apiId, "unit", METRIC.toString()));
    }

    public WeatherDto getByCity(String city) {
        cityQueryParam.put(QUERY_PARAM_CITY, city);
        final ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(apiUrl, WeatherResponse.class, cityQueryParam);
        log.debug("WeatherResponse:{}", response);
        final WeatherEntity saved = weatherRepository.save(weatherResponseConverter.convert(response.getBody()));
        log.debug("Weather saved as:{}", saved);
        return weatherEntityConverter.convert(saved);
    }

    @Override
    public List<WeatherDto> getByCityAll(String city) {
        return convertEntities(weatherRepository.findByCity(city));

    }

    @Override
    public List<WeatherDto> getByCityMostRecent(String city) {
        return convertEntities(weatherRepository.findTopByAndCityOrderByIdDesc(city));

    }

    private List<WeatherDto> convertEntities(List<WeatherEntity> entities) {
        return entities.stream()
            .map(weatherEntityConverter::convert)
            .collect(Collectors.toList());
    }
}
