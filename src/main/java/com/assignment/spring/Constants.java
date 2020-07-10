package com.assignment.spring;

/**
 * Don't use constants from this class, inject from application.propeties.
 * @deprecated
 * @see src/main/resources/application.propeties
 */
@Deprecated
public class Constants {

    public static final String APP_ID = "TBD";

    public static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&APPID={appid}";
}
