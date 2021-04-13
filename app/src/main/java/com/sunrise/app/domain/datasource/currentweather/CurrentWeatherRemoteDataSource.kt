package com.sunrise.app.domain.datasource.currentweather

import com.sunrise.app.domain.datasource.OpenWeatherMapApi
import com.sunrise.app.domain.model.CurrentWeatherResponse
import io.reactivex.Single
import javax.inject.Inject

class CurrentWeatherRemoteDataSource @Inject constructor(private val api: OpenWeatherMapApi) {

    fun getCurrentWeatherByGeoCords(
        lat: Double,
        lon: Double,
        units: String
    ): Single<CurrentWeatherResponse> = api.getCurrentWeatherByGeoCords(lat, lon, units)

}