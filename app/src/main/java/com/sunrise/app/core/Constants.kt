package com.sunrise.app.core

import com.sunrise.app.BuildConfig

object Constants {

    object Service {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val API_KEY_VALUE = BuildConfig.OPEN_WEATHER_MAP_API_KEY
        const val RATE_LIMITER_TYPE = "data"
        const val API_KEY_QUERY = "appid"
    }

    object Coords {
        const val LAT = "lat"
        const val LON = "lon"
        const val METRIC = "metric"
    }

}