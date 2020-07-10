package com.assignment.spring.dto;

/**
 * Enum class for openweather units. Openweather returns temprature regarding unit.
 * {@link #DEFAULT} : Kelvin
 * {@link #METRIC} : Celsius
 * {@link #IMPERIAL} : Fahrenheit
 */
public enum Unit {
    DEFAULT, METRIC, IMPERIAL
}
