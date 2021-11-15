package com.example.lifestyleapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.lifestyleapp.user.WeatherEntity
import com.example.lifestyleapp.weather.WeatherData
import com.example.lifestyleapp.repository.WeatherRepository


class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    //
    var repository: WeatherRepository = WeatherRepository(application)
    var weatherData: MutableLiveData<WeatherData> = WeatherRepository.weatherData

    // for mapping json objects

    fun addWeather(weatherEntity: WeatherEntity){
        WeatherRepository.addWeather(weatherEntity)
    }

    fun updateWeather(weatherEntity: WeatherEntity){
        WeatherRepository.updateWeather(weatherEntity)
    }

}