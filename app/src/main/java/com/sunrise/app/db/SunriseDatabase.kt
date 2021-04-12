package com.sunrise.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrise.app.db.dao.CurrentWeatherDao
import com.sunrise.app.db.dao.ForecastDao
import com.sunrise.app.db.entity.CurrentWeatherEntity
import com.sunrise.app.db.entity.ForecastEntity
import com.sunrise.app.utils.DataConverter

@Database(
    entities = [
        CurrentWeatherEntity::class,
        ForecastEntity::class
    ],
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class SunriseDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao

    abstract fun forecastDao(): ForecastDao
}
