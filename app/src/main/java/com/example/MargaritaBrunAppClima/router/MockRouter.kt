package com.example.MargaritaBrunAppClima.router

class MockRouter : Router {
    override fun navegar(ruta: Ruta) {
        when (ruta) {
            Ruta.Ciudades -> println("Navegar a: ${ruta.id}")
            is Ruta.ClimaFloat -> {
                println("Navegar a: ${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}")
            }
            is Ruta.ClimaDouble -> {
                println("Navegar a: ${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}")
            }
        }
    }
}
