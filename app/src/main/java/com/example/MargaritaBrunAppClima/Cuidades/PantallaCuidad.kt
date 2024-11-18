package com.example.MargaritaBrunAppClima.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.MargaritaBrunAppClima.api.City
import com.example.MargaritaBrunAppClima.api.RetrofitClient
import com.example.MargaritaBrunAppClima.router.Router
import com.example.MargaritaBrunAppClima.router.Ruta
import kotlinx.coroutines.launch
import com.example.MargaritaBrunAppClima.api.Coord
import com.example.MargaritaBrunAppClima.ApiConfig
import com.example.MargaritaBrunAppClima.Clima.lightGray

@Composable
fun PantallaCuidad(router: Router, latitude: Double?, longitude: Double?) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var cities by remember { mutableStateOf<List<City>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showNoResultsError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val popularCities = listOf(
        City(name = "Roma", coord = Coord(41.9028f, 12.4964f)),
        City(name = "Berlín", coord = Coord(52.5200f, 13.4050f)),
        City(name = "Madrid", coord = Coord(40.4168f, -3.7038f)),
        City(name = "El Cairo", coord = Coord(30.0444f, 31.2357f))
    )

    fun BuscarCuidades(query: String, showError: Boolean = false) {
        if (query.isNotEmpty()) {
            isLoading = true
            errorMessage = null
            coroutineScope.launch {
                try {
                    val response = RetrofitClient.weatherServiceV2.searchCities(query, ApiConfig.apiKey)
                    cities = response.list ?: emptyList()
                    showNoResultsError = showError && cities.isEmpty()
                    errorMessage = if (showNoResultsError) "Ciudad no encontrada." else null
                } catch (e: Exception) {
                    errorMessage = if (showError) "Error al buscar las ciudades." else null
                } finally {
                    isLoading = false
                }
            }
        } else {
            cities = emptyList()
            errorMessage = null
            showNoResultsError = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                BuscarCuidades(searchQuery.text)
                showNoResultsError = false
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { BuscarCuidades(searchQuery.text, showError = true) },
                colors = ButtonDefaults.buttonColors(containerColor = lightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar")
            }

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2)),
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
            ) {
                Text(
                    text = it,
                    color = Color(0xFFB68B8B),
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        }

        if (isLoading) {
            Text("Cargando...")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (cities.isNotEmpty()) {
                    item {
                        Text("Resultados de Búsqueda", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(cities) { city ->
                        Card(
                            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                router.navegar(Ruta.ClimaDouble(city.coord.lat.toDouble(), city.coord.lon.toDouble(), city.name))
                            },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Text(text = city.name, modifier = Modifier.padding(16.dp), style = TextStyle(fontSize = 16.sp))
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Ciudades Populares", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(popularCities) { city ->
                    Card(
                        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                            router.navegar(Ruta.ClimaDouble(city.coord.lat.toDouble(), city.coord.lon.toDouble(), city.name))
                        },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(text = city.name, modifier = Modifier.padding(16.dp), style = TextStyle(fontSize = 16.sp))
                    }
                }
            }
         }
        }
    }
}




