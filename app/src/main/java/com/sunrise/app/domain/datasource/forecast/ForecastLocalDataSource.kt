package com.sunrise.app.domain.datasource.forecast

import com.sunrise.app.db.dao.ForecastDao
import com.sunrise.app.db.entity.ForecastEntity
import com.sunrise.app.domain.model.ForecastResponse
import javax.inject.Inject

class ForecastLocalDataSource @Inject constructor(private val forecastDao: ForecastDao) {

    fun getForecast() = forecastDao.getForecast()

    fun insertForecast(forecast: ForecastResponse) =
        forecastDao.deleteAndInsert(ForecastEntity(forecast))

}
