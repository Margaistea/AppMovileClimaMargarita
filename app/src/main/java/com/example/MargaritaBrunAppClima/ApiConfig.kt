package com.example.MargaritaBrunAppClima

import com.example.MargaritaBrunAppClima.BuildConfig

object ApiConfig {
    val apiKey: String
        get() = BuildConfig.WEATHER_API_KEY
}