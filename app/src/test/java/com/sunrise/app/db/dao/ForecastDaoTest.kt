package com.sunrise.app.db.dao

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sunrise.app.db.SunriseDatabase
import com.sunrise.app.db.entity.CityEntity
import com.sunrise.app.db.entity.CoordEntity
import com.sunrise.app.utils.createSampleForecastEntity
import com.sunrise.app.utils.getOrAwaitValue
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class ForecastDaoTest {

    private lateinit var database: SunriseDatabase
    private lateinit var forecastDao: ForecastDao

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
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

        Assert.assertNull(forecast)
    }

    @Test
    fun `getForecast should return a the forecast if the database is not empty`() {
        forecastDao.insertForecast(createSampleForecastEntity(1, "Johannesburg"))

        val forecast = forecastDao.getForecast()

        Assert.assertNotNull(forecast)
    }

    @Test
    fun `getForecastByCoord should return null if there is not forecast in the database`() {
        val forecast = forecastDao.getForecastByCoord(354.0, 465.0).getOrAwaitValue()

        Assert.assertNull(forecast)
    }

    @Test
    fun `getForecastByCoord should return a the forecast if the database is not empty`() {
        forecastDao.insertForecast(createSampleForecastEntity(1, "Johannesburg"))

        val forecast = forecastDao.getForecastByCoord(354.0, 465.0)

        Assert.assertNotNull(forecast)
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
