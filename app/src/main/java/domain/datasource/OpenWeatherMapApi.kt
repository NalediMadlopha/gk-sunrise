package domain.datasource

import domain.model.CurrentWeatherResponse
import domain.model.ForecastResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("weather")
    fun getCurrentWeatherByGeoCords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ): Single<CurrentWeatherResponse>

    @GET("forecast")
    fun getForecastByGeoCords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ): Single<ForecastResponse>

}
