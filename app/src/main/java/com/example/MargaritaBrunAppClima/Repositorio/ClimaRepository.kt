package com.example.MargaritaBrunAppClima.Repositorio

import android.util.Log
import com.example.MargaritaBrunAppClima.ApiConfig
import com.example.MargaritaBrunAppClima.api.WeeklyForecastResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

object WeatherRepository {
    private val clientV2 = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }
    private val clientV3 = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }
    suspend fun fetchSevenDayForecast(lat: Double, lon: Double): WeeklyForecastResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = clientV3.get("https://api.openweathermap.org/data/3.0/onecall") {
                    parameter("lat", lat)
                    parameter("lon", lon)
                    parameter("appid", ApiConfig.apiKey)
                    parameter("units", "metric")
                }

                if (response.status.value == 200) {
                    val responseBody: String = response.bodyAsText()
                    Log.d("WeatherRepository", "7-day forecast response: $responseBody")
                    return@withContext Json.decodeFromString<WeeklyForecastResponse>(responseBody)
                } else {
                    Log.e("WeatherRepository", "Error: ${response.status.value}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("WeatherRepository", "Error al obtener el pronóstico de 7 días: ${e.message}")
                return@withContext null
            }
        }
    }
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = clientV2.get("https://api.openweathermap.org/data/2.5/weather") {
                    parameter("lat", lat)
                    parameter("lon", lon)
                    parameter("appid", ApiConfig.apiKey)
                    parameter("units", "metric")
                }

                if (response.status.value == 200) {
                    val responseBody: String = response.bodyAsText()
                    Log.d("WeatherRepository", "Current weather response: $responseBody")
                    return@withContext Json.decodeFromString<WeatherResponse>(responseBody)
                } else {
                    Log.e("WeatherRepository", "Error: ${response.status.value}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("WeatherRepository", "Error al obtener el clima : ${e.message}")
                return@withContext null
            }
        }
    }
}


@Serializable
data class CityCoordinates(
    val coord: Coord
)

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)

@Serializable
data class WeeklyForecastResponse(
    val daily: List<DailyForecastItem>
)

@Serializable
data class DailyForecastItem(
    val dt: Long,
    val temp: Temperature,
    val weather: List<WeatherDescription>
)

@Serializable
data class Temperature(
    val day: Float,
    val min: Float,
    val max: Float
)

@Serializable
data class WeatherDescription(
    val description: String
)

@Serializable
data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String,
    val sys: Sys,
    val wind: Wind
)

@Serializable
data class Main(
    val temp: Float,
    val temp_max: Float,
    val temp_min: Float,
    val pressure: Float,
    val humidity: Float
)

@Serializable
data class Weather(
    val description: String,
    val icon: String
)

@Serializable
data class Sys(
    val country: String
)

@Serializable
data class Wind(
    val speed: Float
)


