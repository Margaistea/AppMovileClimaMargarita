package com.example.MargaritaBrunAppClima.Clima

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.MargaritaBrunAppClima.api.WeatherResponse
import com.example.MargaritaBrunAppClima.api.RetrofitClient
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.MargaritaBrunAppClima.api.WeeklyForecastResponse
import com.example.MargaritaBrunAppClima.router.Router
import com.example.MargaritaBrunAppClima.router.Ruta
import com.example.MargaritaBrunAppClima.ApiConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

@Composable
fun TemperatureChart(
    maxTemperatures: List<Double>,
    minTemperatures: List<Double>,
    days: List<String>
) {
    val barHeight = 15.dp
    val barHeightPx = with(LocalDensity.current) { barHeight.toPx() }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth().height(250.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            days.forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    modifier = Modifier.width(40.dp),
                    maxLines = 1
                )
            }
        }
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val heightPerDay = size.height / days.size
            val maxTemp = maxTemperatures.maxOrNull()?.toFloat() ?: 1f
            val minTemp = minTemperatures.minOrNull()?.toFloat() ?: 0f
            val tempRange = maxTemp - minTemp

            for (i in maxTemperatures.indices) {
                val yOffset = i * heightPerDay + heightPerDay / 2
                val maxTempWidth = ((maxTemperatures[i].toFloat() - minTemp) / tempRange) * size.width
                val minTempWidth = ((minTemperatures[i].toFloat() - minTemp) / tempRange) * size.width

                drawLine(
                    color = Color(0xFF567BEE),
                    start = Offset(0f, yOffset - barHeightPx / 2),
                    end = Offset(maxTempWidth, yOffset - barHeightPx / 2),
                    strokeWidth = barHeightPx
                )
                drawLine(
                    color = Color(0xFF567BEE),
                    start = Offset(0f, yOffset + barHeightPx / 2),
                    end = Offset(minTempWidth, yOffset + barHeightPx / 2),
                    strokeWidth = barHeightPx
                )
            }
        }
    }
}
val lightGray = Color(0xFFD3D3D3)
@Composable
fun WeatherScreen(lat: Double, lon: Double, cityName: String, router: Router) {
    var currentWeather by remember { mutableStateOf<WeatherResponse?>(null) }
    var sevenDayForecast by remember { mutableStateOf<WeeklyForecastResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(cityName) {
        isFavorite = UserPreferences.getFavoriteCity(context) == cityName
    }

    LaunchedEffect(lat, lon) {
        isLoading = true
        try {
            val currentResponse = RetrofitClient.weatherServiceV2.searchWeatherByCoordinates(
                lat, lon, ApiConfig.apiKey
            )
            currentWeather = currentResponse

            val sevenDayForecastResponse = RetrofitClient.weatherServiceV3.getSevenDayForecast(
                lat, lon, ApiConfig.apiKey, "metric"
            )
            sevenDayForecast = sevenDayForecastResponse
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error al obtener el pronóstico."
            Log.e("WeatherScreen", "Error: ${e.message}")
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            Text("Cargando pronóstico para $cityName...")
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            currentWeather?.let { weather ->
                Text("Clima actual en $cityName:", style = TextStyle(fontWeight = FontWeight.Bold))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Temperatura: ${weather.main.temp}°C", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                        val weatherDescription = weather.weather.firstOrNull()?.description ?: "Sin descripción"
                        Text("Descripción: $weatherDescription")
                        Text("Humedad: ${weather.main.humidity}%")
                        Text("Viento: ${weather.wind.speed} m/s", style = TextStyle(fontSize = 16.sp))
                        Text("Temp. máxima: ${weather.main.temp_max}°C", style = TextStyle(fontSize = 16.sp))
                        Text("Temp. mínima: ${weather.main.temp_min}°C", style = TextStyle(fontSize = 16.sp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            sevenDayForecast?.daily?.let { weeklyForecastList ->
                Text("Pronóstico de la semana para $cityName:", style = TextStyle(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))

                val maxTemperatures = weeklyForecastList.take(7).map { it.temp.max }
                val minTemperatures = weeklyForecastList.take(7).map { it.temp.min }
                val days = weeklyForecastList.take(7).map { item ->
                    val date = Date(item.dt * 1000)
                    SimpleDateFormat("EEE", Locale.getDefault()).format(date)
                }

                TemperatureChart(maxTemperatures, minTemperatures, days)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(weeklyForecastList.take(7)) { item ->
                        val date = Date(item.dt * 1000)
                        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val formattedDate = formatter.format(date)

                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(formattedDate, style = TextStyle(fontWeight = FontWeight.Bold))
                                Text("Temperatura: ${item.temp.day}°C", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                                Text("Temp. máxima: ${item.temp.max}°C", style = TextStyle(fontSize = 16.sp))
                                Text("Temp. mínima: ${item.temp.min}°C", style = TextStyle(fontSize = 16.sp))
                                val weatherDescription = item.weather.firstOrNull()?.description ?: "Sin descripción"
                                Text("Descripción: $weatherDescription")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { router.navegar(Ruta.Ciudades) },
                colors = ButtonDefaults.buttonColors(containerColor = lightGray),
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
            ) {
                Text("Volver a Ciudades")
            }

            Button(
                onClick = {
                    val shareText = buildString {
                        append("Pronóstico del clima para $cityName:\n")
                        currentWeather?.let {
                            append("Temperatura actual: ${it.main.temp}°C\n")
                            append("Descripción: ${it.weather.firstOrNull()?.description ?: "Sin descripción"}\n")
                        }
                        sevenDayForecast?.daily?.let { weeklyForecast ->
                            append("\nPronóstico semanal:\n")
                            weeklyForecast.take(7).forEach { item ->
                                val date = Date(item.dt * 1000)
                                val formattedDate = SimpleDateFormat("dd MMM", Locale.getDefault()).format(date)
                                append("$formattedDate - Máx: ${item.temp.max}°C, Mín: ${item.temp.min}°C\n")
                            }
                        }
                    }
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Compartir pronóstico"))
                },
                colors = ButtonDefaults.buttonColors(containerColor = lightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compartir Clima")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            if (isFavorite) {
                Button(
                    onClick = {
                        UserPreferences.clearFavoriteCity(context)
                        isFavorite = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = lightGray),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                ) {
                    Text("Eliminar de Favoritos")
                }
            } else {
                Button(
                    onClick = {
                        UserPreferences.saveFavoriteCity(context, cityName)
                        isFavorite = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = lightGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Marcar como Favorito")
                }
            }
        }
    }
}


