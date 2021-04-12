package com.sunrise.app.domain.datasource.forecast

import com.sunrise.app.domain.datasource.OpenWeatherMapApi
import com.sunrise.app.domain.model.ForecastResponse
import io.reactivex.Single
import javax.inject.Inject

class ForecastRemoteDataSource @Inject constructor(private val api: OpenWeatherMapApi) {

    fun getForecastByGeoCords(lat: Double, lon: Double, units: String): Single<ForecastResponse> =
        api.getForecastByGeoCords(lat, lon, units)

}