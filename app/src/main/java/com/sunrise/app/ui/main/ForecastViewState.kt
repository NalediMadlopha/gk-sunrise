package com.sunrise.app.ui.main

import com.sunrise.app.core.BaseViewState
import com.sunrise.app.db.entity.ForecastEntity
import com.sunrise.app.utils.domain.Status

class ForecastViewState(
    val status: Status,
    val error: String? = null,
    val data: ForecastEntity? = null
) : BaseViewState(status, error) {
    fun getForecast() = data
}