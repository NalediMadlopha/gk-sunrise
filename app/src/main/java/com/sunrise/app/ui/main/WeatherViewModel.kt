package com.sunrise.app.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunrise.app.core.Constants
import com.sunrise.app.domain.usercase.CurrentWeatherUseCase
import com.sunrise.app.domain.usercase.ForecastUseCase
import javax.inject.Inject

class WeatherViewModel @Inject internal constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val forecastUseCase: ForecastUseCase,
    var sharedPreferences: SharedPreferences
): ViewModel() {

    private val currentWeatherParams = MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams>()
    private val forecastParams = MutableLiveData<ForecastUseCase.ForecastParams>()

    fun setCurrentWeatherParams(params: CurrentWeatherUseCase.CurrentWeatherParams) {
        if (currentWeatherParams.value == params) {
            return
        }
        currentWeatherParams.postValue(params)
    }

    fun getCurrentWeatherViewState() = currentWeatherViewState

    fun setForecastParams(params: ForecastUseCase.ForecastParams) {
        if (forecastParams.value == params) {
            return
        }
        forecastParams.postValue(params)
    }

    fun getForecastViewState() = forecastViewState

    fun setCoordinates(lat: Double?, lon: Double?) {
        sharedPreferences.let { sharedPreferences ->
            sharedPreferences.edit().let { editor ->
                editor.putString(Constants.Coords.LAT, lat.toString())
                editor.putString(Constants.Coords.LON, lon.toString())
            }.apply()
        }
    }

    private val currentWeatherViewState: LiveData<CurrentWeatherViewState> = currentWeatherParams.switchMap { params ->
        currentWeatherUseCase.execute(params)
    }

    private val forecastViewState: LiveData<ForecastViewState> = forecastParams.switchMap { params ->
        forecastUseCase.execute(params)
    }

}