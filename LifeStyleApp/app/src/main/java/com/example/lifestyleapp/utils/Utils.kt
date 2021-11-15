package com.example.lifestyleapp.utils

import android.content.Context
import android.widget.Toast
import java.lang.Exception
import java.lang.reflect.Field
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

class Utils {

    companion object {
        fun md5(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }

    /**
     * Takes in date time as long and returns day of week as string
     * @dt: dateTime in epoch seconds (as long)
     * @tz: timezone of returned data (necessary for converting to correct timezone and whether in daylight savings or not). This can be found in the return json under timezone field
     */
    fun getDay(dt: Long?, tz: String?): String {
        if (dt != null && tz != null) {
            return Instant.ofEpochSecond(dt)
                .atZone(ZoneId.of(tz))
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.US)
        }
        return "N/A"
    }

    /**
     * Map of the weather icon Id's
     */
    private val weatherMapping = mapOf(
        "200" to "thunderstorm",
        "201" to "thunderstorm",
        "202" to "thunderstorm",
        "210" to "lightning",
        "211" to "lightning",
        "212" to "lightning",
        "221" to "lightning",
        "230" to "thunderstorm",
        "231" to "thunderstorm",
        "232" to "thunderstorm",
        "300" to "sprinkle",
        "301" to "sprinkle",
        "302" to "rain",
        "310" to "rain_mix",
        "311" to "rain",
        "312" to "rain",
        "313" to "showers",
        "314" to "rain",
        "321" to "sprinkle",
        "500" to "sprinkle",
        "501" to "rain",
        "502" to "rain",
        "503" to "rain",
        "504" to "rain",
        "511" to "rain_mix",
        "520" to "showers",
        "521" to "showers",
        "522" to "showers",
        "531" to "storm_showers",
        "600" to "snow",
        "601" to "snow",
        "602" to "sleet",
        "611" to "rain_mix",
        "612" to "rain_mix",
        "615" to "rain_mix",
        "616" to "rain_mix",
        "620" to "rain_mix",
        "621" to "snow",
        "622" to "snow",
        "701" to "showers",
        "711" to "smoke",
        "721" to "day_haze",
        "731" to "dust",
        "741" to "fog",
        "761" to "dust",
        "762" to "dust",
        "771" to "cloudy_gusts",
        "781" to "tornado",
        "800" to "day_sunny",
        "801" to "cloudy_gusts",
        "802" to "cloudy_gusts",
        "803" to "cloudy_gusts",
        "804" to "cloudy",
        "900" to "tornado",
        "901" to "storm_showers",
        "902" to "hurricane",
        "903" to "snowflake_cold",
        "904" to "hot",
        "905" to "windy",
        "906" to "hail",
        "957" to "strong_wind",
        "day_200" to "day_thunderstorm",
        "day_201" to "day_thunderstorm",
        "day_202" to "day_thunderstorm",
        "day_210" to "day_lightning",
        "day_211" to "day_lightning",
        "day_212" to "day_lightning",
        "day_221" to "day_lightning",
        "day_230" to "day_thunderstorm",
        "day_231" to "day_thunderstorm",
        "day_232" to "day_thunderstorm",
        "day_300" to "day_sprinkle",
        "day_301" to "day_sprinkle",
        "day_302" to "day_rain",
        "day_310" to "day_rain",
        "day_311" to "day_rain",
        "day_312" to "day_rain",
        "day_313" to "day_rain",
        "day_314" to "day_rain",
        "day_321" to "day_sprinkle",
        "day_500" to "day_sprinkle",
        "day_501" to "day_rain",
        "day_502" to "day_rain",
        "day_503" to "day_rain",
        "day_504" to "day_rain",
        "day_511" to "day_rain_mix",
        "day_520" to "day_showers",
        "day_521" to "day_showers",
        "day_522" to "day_showers",
        "day_531" to "day_storm_showers",
        "day_600" to "day_snow",
        "day_601" to "day_sleet",
        "day_602" to "day_snow",
        "day_611" to "day_rain_mix",
        "day_612" to "day_rain_mix",
        "day_615" to "day_rain_mix",
        "day_616" to "day_rain_mix",
        "day_620" to "day_rain_mix",
        "day_621" to "day_snow",
        "day_622" to "day_snow",
        "day_701" to "day_showers",
        "day_711" to "smoke",
        "day_721" to "day_haze",
        "day_731" to "dust",
        "day_741" to "day_fog",
        "day_761" to "dust",
        "day_762" to "dust",
        "day_781" to "tornado",
        "day_800" to "day_sunny",
        "day_801" to "day_cloudy_gusts",
        "day_802" to "day_cloudy_gusts",
        "day_803" to "day_cloudy_gusts",
        "day_804" to "day_sunny_overcast",
        "day_900" to "tornado",
        "day_902" to "hurricane",
        "day_903" to "snowflake_cold",
        "day_904" to "hot",
        "day_906" to "day_hail",
        "day_957" to "strong_wind",
        "night_200" to "night_alt_thunderstorm",
        "night_201" to "night_alt_thunderstorm",
        "night_202" to "night_alt_thunderstorm",
        "night_210" to "night_alt_lightning",
        "night_211" to "night_alt_lightning",
        "night_212" to "night_alt_lightning",
        "night_221" to "night_alt_lightning",
        "night_230" to "night_alt_thunderstorm",
        "night_231" to "night_alt_thunderstorm",
        "night_232" to "night_alt_thunderstorm",
        "night_300" to "night_alt_sprinkle",
        "night_301" to "night_alt_sprinkle",
        "night_302" to "night_alt_rain",
        "night_310" to "night_alt_rain",
        "night_311" to "night_alt_rain",
        "night_312" to "night_alt_rain",
        "night_313" to "night_alt_rain",
        "night_314" to "night_alt_rain",
        "night_321" to "night_alt_sprinkle",
        "night_500" to "night_alt_sprinkle",
        "night_501" to "night_alt_rain",
        "night_502" to "night_alt_rain",
        "night_503" to "night_alt_rain",
        "night_504" to "night_alt_rain",
        "night_511" to "night_alt_rain_mix",
        "night_520" to "night_alt_showers",
        "night_521" to "night_alt_showers",
        "night_522" to "night_alt_showers",
        "night_531" to "night_alt_storm_showers",
        "night_600" to "night_alt_snow",
        "night_601" to "night_alt_sleet",
        "night_602" to "night_alt_snow",
        "night_611" to "night_alt_rain_mix",
        "night_612" to "night_alt_rain_mix",
        "night_615" to "night_alt_rain_mix",
        "night_616" to "night_alt_rain_mix",
        "night_620" to "night_alt_rain_mix",
        "night_621" to "night_alt_snow",
        "night_622" to "night_alt_snow",
        "night_701" to "night_alt_showers",
        "night_711" to "smoke",
        "night_721" to "day_haze",
        "night_731" to "dust",
        "night_741" to "night_fog",
        "night_761" to "dust",
        "night_762" to "dust",
        "night_781" to "tornado",
        "night_800" to "night_clear",
        "night_801" to "night_alt_cloudy_gusts",
        "night_802" to "night_alt_cloudy_gusts",
        "night_803" to "night_alt_cloudy_gusts",
        "night_804" to "night_alt_cloudy",
        "night_900" to "tornado",
        "night_902" to "hurricane",
        "night_903" to "snowflake_cold",
        "night_904" to "hot",
        "night_906" to "night_alt_hail",
        "night_957" to "strong_wind"
    )

    /**
     * takes in the weather icon ID and returns a string of the icon to be used.
     */
    fun getIcon(id: Int?, daytime: Boolean): String {
        var weatherString: String = ""
        weatherString = if (daytime) {
            weatherMapping.getValue("day_$id");
        } else weatherMapping.getValue("night_$id")
        println(id)
        println(weatherString)
        return "wi_$weatherString";
    }

    /**
     * takes in sunrise and sunset epoch times for that day and returns whether it's daylight hours or not.
     */
    fun isDayTime(sunrise: Long?, sunset: Long?): Boolean {
        val now = System.currentTimeMillis() / 1000
        return now in sunrise!!..sunset!!
    }

    fun getResId(resName: String?, c: Class<*>): Int {
        return try {
            val idField: Field = c.getDeclaredField(resName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    fun warnIfOverTwo(lbsToLose: Int, context: Context) {

        Toast.makeText(
            context,
            "warning: doctors recommend not trying to lose over 2 lbs per week",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}