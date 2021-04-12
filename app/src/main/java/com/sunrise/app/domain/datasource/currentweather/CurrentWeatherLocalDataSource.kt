package com.sunrise.app.domain.datasource.currentweather

import com.sunrise.app.db.dao.CurrentWeatherDao
import com.sunrise.app.db.entity.CurrentWeatherEntity
import com.sunrise.app.domain.model.CurrentWeatherResponse
import javax.inject.Inject

class CurrentWeatherLocalDataSource @Inject constructor(private val currentWeatherDao: CurrentWeatherDao) {

    fun getCurrentWeather() = currentWeatherDao.getCurrentWeather()

    fun insertCurrentWeather(currentWeather: CurrentWeatherResponse) =
        currentWeatherDao.deleteAndInsert(
            CurrentWeatherEntity(currentWeather)
        )

}
