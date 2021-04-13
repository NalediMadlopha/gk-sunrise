package com.sunrise.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunrise.app.domain.usercase.CurrentWeatherUseCase
import com.sunrise.app.domain.usercase.ForecastUseCase
import javax.inject.Inject

class MainFragmentViewModel @Inject internal constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val forecastUseCase: ForecastUseCase
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

    private val currentWeatherViewState: LiveData<CurrentWeatherViewState> = currentWeatherParams.switchMap { params ->
        currentWeatherUseCase.execute(params)
    }

    private val forecastViewState: LiveData<ForecastViewState> = forecastParams.switchMap { params ->
        forecastUseCase.execute(params)
    }

}