package com.sunrise.app.di.module

import android.content.Context
import androidx.room.Room
import com.sunrise.app.db.SunriseDatabase
import com.sunrise.app.db.dao.CurrentWeatherDao
import com.sunrise.app.db.dao.ForecastDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getDatabase(context: Context): SunriseDatabase {
        return Room.databaseBuilder(
            context,
            SunriseDatabase::class.java, "Sunrise-DB"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(database: SunriseDatabase): CurrentWeatherDao {
        return database.currentWeatherDao()
    }

    @Singleton
    @Provides
    fun provideForecastDao(database: SunriseDatabase): ForecastDao {
        return database.forecastDao()
    }

}
