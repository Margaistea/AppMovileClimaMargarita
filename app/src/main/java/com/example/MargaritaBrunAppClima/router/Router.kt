package com.example.MargaritaBrunAppClima.router

import androidx.navigation.NavHostController

interface Router {
    fun navegar(ruta: Ruta)
}

class Enrutador(
    private val navHostController: NavHostController
) : Router {
    override fun navegar(ruta: Ruta) {
        when (ruta) {

            Ruta.Ciudades -> navHostController.navigate(ruta.id)

            is Ruta.ClimaFloat -> {
                val route = "${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}"
                navHostController.navigate(route)
            }

            is Ruta.ClimaDouble -> {
                val route = "${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}"
                navHostController.navigate(route)
            }
        }
    }
}

