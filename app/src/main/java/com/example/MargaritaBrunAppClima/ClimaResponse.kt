package com.example.MargaritaBrunAppClima.api

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String,
    val sys: Sys,
    val wind: Wind
)

data class Weather(
    val description: String,
    val icon: String
)

data class Main(
    val temp: Float,
    val temp_max: Float,
    val temp_min: Float,
    val pressure: Float,
    val humidity: Float
)

data class Sys(
    val country: String
)

data class CitySearchResponse(
    val list: List<City>
)

data class City(
    val name: String,
    val coord: Coord
)

data class Wind(
    val speed: Float
)

data class Coord(
    val lat: Float,
    val lon: Float
)

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>
)

data class DailyForecastResponse(
    val daily: List<DailyForecastItem>
)

data class DailyForecastItem(
    val dt: Long,
    val temp: Temperature,
    val weather: List<Weather>
)

data class WeeklyForecastResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val daily: List<DailyForecast>
)

data class DailyForecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<Weather>
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)


data class Temperature(
    val day: Float,
    val min: Float,
    val max: Float
)
