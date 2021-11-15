package com.example.lifestyleapp.repository

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R
import com.example.lifestyleapp.user.*
import com.example.lifestyleapp.database.UserDatabase
import com.example.lifestyleapp.weather.WeatherData

import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL


object WeatherRepository {

    private lateinit var res: Resources
    var weatherData: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    private lateinit var userDao: UserDao

    operator fun invoke(context: Context): WeatherRepository { // this is like get instance
        res = context.resources
        userDao = UserDatabase.getDatabase(context).userDao()
        return this
    }


    init {
        println("INIT WEATHER VIEW MODEL")
        val weatherInitial = WeatherData()
        weatherData = MutableLiveData<WeatherData>().default(weatherInitial)
        doAsync { updateWeather() }
    }

    // set weather data values
    fun updateWeather() {
        val weatherObj = makeWeatherDataObj()
        weatherData.value?.jsonWeatherObj = weatherObj
        weatherData.postValue(weatherData.value)
    }

    private fun makeWeatherDataObj(): JSONObject {
        val jsonObject = makeWeatherAPICall()
        return jsonObject
    }


    private fun makeWeatherAPICall(): JSONObject {
        //https://stackoverflow.com/questions/47628646/how-should-i-get-resourcesr-string-in-viewmodel-in-android-mvvm-and-databindi
        val apiKey =
            res.getString(R.string.API_KEY)// open weather api key
        val lat = res.getString(R.string.latitude)
        val log = res.getString(R.string.longitude)
        val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$log&appid=$apiKey&units=imperial&exclude=hourly"
        val resultJson = URL(url).readText()
        updateWeather(WeatherEntity(MainActivity.mUserViewModel.currentUser.value?.userName!!, resultJson))
        return JSONObject(resultJson)
    }

    // TODO: Pull this out into  a helper
    private fun <T : Any?> MutableLiveData<T>.default(initialValue: T) =
        apply { setValue(initialValue) }

    fun addWeather(weatherEntity: WeatherEntity){
        userDao.addWeather(weatherEntity)
    }

    fun updateWeather(weatherEntity: WeatherEntity){
        userDao.updateWeather(weatherEntity)
    }


}