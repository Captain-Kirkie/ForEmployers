package com.example.lifestyleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifestyleapp.*
import com.example.lifestyleapp.utils.Utils
import com.example.lifestyleapp.weather.DailyWeather
import com.example.lifestyleapp.weather.WeatherRecyclerAdapter
import java.time.Instant


class WeatherFragment : Fragment() {
    //private lateinit var weatherAdapter: WeatherRecyclerAdapter
    //private lateinit var layoutManager: LinearLayoutManager
    private val helper = Utils()
    var isDayTime: Boolean = false
    private lateinit var weatherRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        var current =
            MainActivity.weatherViewModel.weatherData.value!!.jsonWeatherObj?.getJSONObject("current")

        var sunrise: Long? = current?.getLong("sunrise")
        var sunset: Long? = current?.getLong("sunset")
        if (sunrise != null && sunset != null) {
            isDayTime = helper.isDayTime(sunrise, sunset)
        }

        val weatherAdapter = WeatherRecyclerAdapter(setWeatherArray(helper, isDayTime))
        weatherRecycler = view.findViewById(R.id.rv_4dayWeather)
        weatherRecycler.adapter = weatherAdapter
        weatherRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.weatherViewModel.weatherData.observe(viewLifecycleOwner) {
            setTextViews(helper)
        }
    }

    private fun setTextViews(helper: Utils) {
        setMainTemp()
        setCurrDate(helper)
        setMainIcon(helper, isDayTime)
    }


    private fun setCurrDate(helper: Utils) {
        val curr =
            MainActivity.weatherViewModel.weatherData.value?.jsonWeatherObj?.getJSONObject("current")
        if (curr != null) {
            val date = curr.getLong("dt")
            val timeZone =
                MainActivity.weatherViewModel.weatherData.value?.jsonWeatherObj?.getString("timezone")
            val currDay = helper.getDay(date, timeZone)
            val dayOf =
                date?.let { Instant.ofEpochSecond(it).toString() } // returns: 2018-05-10T17:56:39Z
            val formatted = currDay + "\n" + (dayOf?.substringBefore("T") ?: "N/A")
            val view = view?.findViewById(R.id.textView_Day) as TextView
            view.text = formatted
        }
    }

    private fun setMainIcon(helper: Utils, daytime: Boolean) {
        val id =
            MainActivity.weatherViewModel.weatherData.value?.jsonWeatherObj?.getJSONObject("current")
                ?.getJSONArray("weather")
                ?.getJSONObject(0)?.getInt("id")
        val icon = helper.getIcon(id, daytime)
        val uriStr = "@drawable/$icon" // icon is updating with incorrect thing
        val resID = resources.getIdentifier(uriStr, "drawable", activity?.packageName)
        val view = view?.findViewById(R.id.weather_currentConditionsIcon) as ImageView
        view.setImageResource(resID)
    }

    private fun setMainTemp() {
        val mainTempView = view?.findViewById(R.id.textView5) as TextView
        val temp =
            MainActivity.weatherViewModel.weatherData.value?.jsonWeatherObj?.getJSONObject("current")
                ?.getDouble("temp")
        val fullText = "${temp} \u2109"
        mainTempView.text = fullText
    }

    private fun setWeatherArray(
        helper: Utils,
        daytime: Boolean
    ): ArrayList<DailyWeather> {
        val weather4Array = arrayListOf<DailyWeather>()
        val daily =
            MainActivity.weatherViewModel.weatherData.value!!.jsonWeatherObj?.getJSONArray("daily")
        for (i in 0..3) {
            val tempDay = DailyWeather();
            val currObj = daily!!.getJSONObject(i)
            val icon1 = helper.getIcon(
                currObj.getJSONArray("weather").getJSONObject(0).getInt("id"), daytime
            )
            val uriStr1 = "@drawable/$icon1" // icon is updating with incorrect thing
            val resID1 = resources.getIdentifier(uriStr1, "drawable", activity?.packageName)
            val timeZone =
                MainActivity.weatherViewModel.weatherData.value!!.jsonWeatherObj?.getString("timezone")
            tempDay.day = helper.getDay(currObj.getLong("dt"), timeZone)
            tempDay.iconID = resID1
            tempDay.highTemp = currObj.getJSONObject("temp").getInt("max")
            tempDay.lowTemp = currObj.getJSONObject("temp").getInt("min")
            weather4Array.add(i, tempDay)
        }
        return weather4Array
    }
}