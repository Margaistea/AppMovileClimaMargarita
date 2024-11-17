package com.example.MargaritaBrunAppClima.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceV2 {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse

    @GET("find")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("appid") apiKey: String
    ): CitySearchResponse

    @GET("weather")
    suspend fun searchWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse

}

interface WeatherServiceV3 {

    @GET("onecall")
    suspend fun getSevenDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeeklyForecastResponse
}

