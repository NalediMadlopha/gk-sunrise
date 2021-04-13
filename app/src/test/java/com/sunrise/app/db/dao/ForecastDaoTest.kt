package com.sunrise.app.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sunrise.app.db.SunriseDatabase
import com.sunrise.app.db.entity.CityEntity
import com.sunrise.app.db.entity.CoordEntity
import com.sunrise.app.utils.BaseTest
import com.sunrise.app.utils.createSampleForecastEntity
import com.sunrise.app.utils.getOrAwaitValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ForecastDaoTest : BaseTest() {

    private lateinit var database: SunriseDatabase
    private lateinit var forecastDao: ForecastDao

    @Before
    override fun setUp() {
        super.setUp()
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SunriseDatabase::class.java
        ).allowMainThreadQueries().build()

        forecastDao = database.forecastDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `getForecast should return null if there is not forecast in the database`() {
        val forecast = forecastDao.getForecast().getOrAwaitValue()

        assertNull(forecast)
    }

    @Test
    fun `getForecast should return a the forecast if the database is not empty`() {
        forecastDao.insertForecast(createSampleForecastEntity(1, "Johannesburg"))

        val forecast = forecastDao.getForecast()

        assertNotNull(forecast)
    }

    @Test
    fun `getForecastByCoord should return null if there is not forecast in the database`() {
        val forecast = forecastDao.getForecastByCoord(354.0, 465.0).getOrAwaitValue()

        assertNull(forecast)
    }

    @Test
    fun `getForecastByCoord should return a the forecast if the database is not empty`() {
        forecastDao.insertForecast(createSampleForecastEntity(1, "Johannesburg"))

        val forecast = forecastDao.getForecastByCoord(354.0, 465.0)

        assertNotNull(forecast)
    }

    @Test
    fun `insertForecast should save the forecast in the database`() {
        val forecast = createSampleForecastEntity(1, "Johannesburg")

        forecastDao.insertForecast(forecast)

        assertEquals(forecast, forecastDao.getForecast().getOrAwaitValue())
    }

    @Test
    fun `deleteAndInsert should delete the current weather in the database and insert a new one`() {
        val johannesburgForecast = createSampleForecastEntity(1, "Johannesburg")
        forecastDao.insertForecast(johannesburgForecast)

        val capeTownForecast = createSampleForecastEntity(2, "Cape Town")
        forecastDao.deleteAndInsert(capeTownForecast)

        assertEquals(capeTownForecast, forecastDao.getForecast().getOrAwaitValue())
    }

    @Test
    fun `updateForecast should update an existing forecast`() {
        val forecast = createSampleForecastEntity(1, "Johannesburg")
        forecastDao.insertForecast(forecast)
        forecast.city = CityEntity("South Africa", CoordEntity(34.0, 30.0), "Cape Town", 34)

        forecastDao.updateForecast(forecast)

        assertEquals(forecast.city!!.cityName,
            forecastDao.getForecast().getOrAwaitValue().city?.cityName
        )
    }

    @Test
    fun `deleteForecast should delete the forecast in the database`() {
        val forecast = createSampleForecastEntity(1, "Johannesburg")
        forecastDao.insertForecast(forecast)

        forecastDao.deleteForecast(forecast)

        assertNull(forecastDao.getForecast().getOrAwaitValue())
    }

    @Test
    fun `deleteAll should delete all the forecast in the database`() {
        val forecast = createSampleForecastEntity(1, "Johannesburg")
        forecastDao.insertForecast(forecast)

        forecastDao.deleteAll()

        assertNull(forecastDao.getForecast().getOrAwaitValue())
    }

}
