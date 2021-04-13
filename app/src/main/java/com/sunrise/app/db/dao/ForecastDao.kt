package com.sunrise.app.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sunrise.app.db.entity.ForecastEntity

@Dao
interface ForecastDao {

    @Query("SELECT * FROM Forecast")
    fun getForecast(): LiveData<ForecastEntity>

    @Query("SELECT * FROM Forecast ORDER BY abs(-:lat) AND abs(-:lon) LIMIT 1")
    fun getForecastByCoord(lat: Double, lon: Double): LiveData<ForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: ForecastEntity)

    @Transaction
    fun deleteAndInsert(forecast: ForecastEntity) {
        deleteAll()
        insertForecast(forecast)
    }

    @Update
    fun updateForecast(forecast: ForecastEntity)

    @Delete
    fun deleteForecast(forecast: ForecastEntity)

    @Query("DELETE FROM Forecast")
    fun deleteAll()

}
