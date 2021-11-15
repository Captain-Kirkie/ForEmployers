package com.example.lifestyleapp.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifestyleapp.R

class WeatherRecyclerAdapter(dailyWeather: ArrayList<DailyWeather>) :
    RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder>() {

    private var weather4Day = dailyWeather

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val dayString : TextView = view.findViewById(R.id.dayWeather_day)
        val dayIcon : ImageView = view.findViewById(R.id.dayWeather_icon)
        val highTemp : TextView = view.findViewById(R.id.dayWeather_high)
        val lowTemp : TextView = view.findViewById(R.id.dayWeather_low)
    }

    fun setData(weather :ArrayList<DailyWeather>){
        this.weather4Day = weather
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_weather_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currDay = weather4Day[position]

        holder.dayString.text = currDay.day
        holder.dayIcon.setImageResource(currDay.iconID)
        holder.highTemp.text = currDay.highTemp.toString()
        holder.lowTemp.text = currDay.lowTemp.toString()

    }

    override fun getItemCount(): Int {
        return weather4Day.size
    }

}