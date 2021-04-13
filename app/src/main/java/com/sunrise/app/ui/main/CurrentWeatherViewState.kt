package com.sunrise.app.ui.main

import com.sunrise.app.core.BaseViewState
import com.sunrise.app.db.entity.CurrentWeatherEntity
import com.sunrise.app.utils.domain.Status

class CurrentWeatherViewState(
    val status: Status,
    val error: String? = null,
    val data: CurrentWeatherEntity? = null
) : BaseViewState(status, error) {
    fun getForecast() = data
}
