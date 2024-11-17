package com.example.MargaritaBrunAppClima.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {
    private const val BASE_URL_V2 = "https://api.openweathermap.org/data/2.5/"
    private const val BASE_URL_V3 = "https://api.openweathermap.org/data/3.0/"

    private val retrofitV2: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_V2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val retrofitV3: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_V3)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherServiceV2: WeatherServiceV2 by lazy {
        retrofitV2.create(WeatherServiceV2::class.java)
    }
    val weatherServiceV3: WeatherServiceV3 by lazy {
        retrofitV3.create(WeatherServiceV3::class.java)
    }
}
