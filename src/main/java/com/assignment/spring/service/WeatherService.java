package com.assignment.spring.service;

import com.assignment.spring.dto.WeatherDto;
import java.util.List;

public interface WeatherService {

    /**
     * Gets the city's weather and saves to repository.
     *
     * @param city
     * @return
     */
    WeatherDto getByCity(String city);

    /**
     * Returns all stored weather information for the city.
     *
     * @param city
     * @return
     */
    List<WeatherDto> getByCityAll(String city);

    /**
     * Returns most recent weather information for the city
     *
     * @param city
     * @return
     */
    List<WeatherDto> getByCityMostRecent(String city);
}
