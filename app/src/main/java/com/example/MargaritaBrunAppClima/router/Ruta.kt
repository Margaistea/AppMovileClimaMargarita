package com.example.MargaritaBrunAppClima.router

sealed class Ruta(val id: String) {
    object Ciudades : Ruta("ciudades")

    data class ClimaFloat(val lat: Float, val lon: Float, val nombre: String) : Ruta(
        "clima_float?lat=$lat&lon=$lon&nombre=$nombre")

    data class ClimaDouble(val lat: Double, val lon: Double, val nombre: String) : Ruta(
        "clima_double?lat=$lat&lon=$lon&nombre=$nombre")

}

