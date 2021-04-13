package com.sunrise.app.repo

import androidx.lifecycle.LiveData
import com.sunrise.app.core.Constants.Service.RATE_LIMITER_TYPE
import com.sunrise.app.db.entity.CurrentWeatherEntity
import com.sunrise.app.domain.datasource.currentweather.CurrentWeatherLocalDataSource
import com.sunrise.app.domain.datasource.currentweather.CurrentWeatherRemoteDataSource
import com.sunrise.app.domain.model.CurrentWeatherResponse
import com.sunrise.app.utils.domain.RateLimiter
import com.sunrise.app.utils.domain.Resource
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(
    private val currentWeatherRemoteDataSource: CurrentWeatherRemoteDataSource,
    private val currentWeatherLocalDataSource: CurrentWeatherLocalDataSource
) {

    private val currentWeatherRateLimit = RateLimiter<String>(30, TimeUnit.SECONDS)

    fun loadCurrentWeatherByGeoCords(lat: Double, lon: Double, fetchRequired: Boolean, units: String): LiveData<Resource<CurrentWeatherEntity>> {
        return object : NetworkBoundResource<CurrentWeatherEntity, CurrentWeatherResponse>() {

            override fun saveCallResult(item: CurrentWeatherResponse) = currentWeatherLocalDataSource.insertCurrentWeather(item)

            override fun shouldFetch(data: CurrentWeatherEntity?): Boolean = fetchRequired

            override fun loadFromDb(): LiveData<CurrentWeatherEntity> = currentWeatherLocalDataSource.getCurrentWeather()

            override fun createCall(): Single<CurrentWeatherResponse> = currentWeatherRemoteDataSource.getCurrentWeatherByGeoCords(lat, lon, units)

            override fun onFetchFailed() = currentWeatherRateLimit.reset(RATE_LIMITER_TYPE)

        }.asLiveData
    }

}
