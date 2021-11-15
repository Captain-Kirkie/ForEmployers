package com.example.lifestyleapp.weather

data class Current(
    val clouds: Int,
    val dew_point: Double,
    val dt: Long,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Long,
    val sunset: Long,
    var temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Int,
    val wind_speed: Double
)