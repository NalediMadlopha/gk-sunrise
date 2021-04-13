package com.sunrise.app.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.sunrise.app.db.SunriseDatabase
import com.sunrise.app.utils.BaseTest
import com.sunrise.app.utils.createSampleCurrentWeatherEntity
import com.sunrise.app.utils.getOrAwaitValue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CurrentWeatherDaoTest : BaseTest() {

    private lateinit var database: SunriseDatabase
    private lateinit var currentWeatherDao: CurrentWeatherDao

    @Before
    override fun setUp() {
        super.setUp()
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            SunriseDatabase::class.java
        ).allowMainThreadQueries().build()

        currentWeatherDao = database.currentWeatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `getCurrentWeather should return null if there is not current weather in the database`() {
        val currentWeather = currentWeatherDao.getCurrentWeather().getOrAwaitValue()

        assertNull(currentWeather)
    }

    @Test
    fun `insertCurrentWeather should save the current weather in the database`() {
        val currentWeather = createSampleCurrentWeatherEntity("Johannesburg", 1)

        currentWeatherDao.insertCurrentWeather(currentWeather)

        assertEquals(currentWeather, currentWeatherDao.getCurrentWeather().getOrAwaitValue())
    }

    @Test
    fun `deleteCurrentWeather should delete the current weather in the database`() {
        val currentWeather = createSampleCurrentWeatherEntity("Johannesburg", 1)
        currentWeatherDao.insertCurrentWeather(currentWeather)

        currentWeatherDao.deleteCurrentWeather()

        assertNull(currentWeatherDao.getCurrentWeather().getOrAwaitValue())
    }

    @Test
    fun `deleteAndInsert should delete the current weather in the database and insert a new one`() {
        val johannesburgCurrentWeather = createSampleCurrentWeatherEntity("Johannesburg", 1)
        currentWeatherDao.insertCurrentWeather(johannesburgCurrentWeather)

        val capeTownCurrentWeather = createSampleCurrentWeatherEntity("Cape Town", 2)
        currentWeatherDao.deleteAndInsert(capeTownCurrentWeather)

        assertEquals(capeTownCurrentWeather, currentWeatherDao.getCurrentWeather().getOrAwaitValue())
    }

}
