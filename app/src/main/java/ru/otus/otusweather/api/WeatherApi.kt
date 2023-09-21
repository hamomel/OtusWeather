package ru.otus.otusweather.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(
        @Query("id") id: String,
        @Query("units") unit: String = "metric"
    ): WeatherResponse
}