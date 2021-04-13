package com.sunrise.app.repo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sunrise.app.BaseTest
import com.sunrise.app.core.Constants
import com.sunrise.app.db.entity.ForecastEntity
import com.sunrise.app.domain.datasource.forecast.ForecastLocalDataSource
import com.sunrise.app.domain.datasource.forecast.ForecastRemoteDataSource
import com.sunrise.app.utils.createSampleForecastEntity
import com.sunrise.app.utils.createSampleForecastResponse
import com.sunrise.app.utils.domain.Resource
import com.sunrise.app.utils.domain.Status
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ForecastRepositoryTest : BaseTest() {

    private lateinit var repository: ForecastRepository

    @MockK
    private lateinit var mockForecastRemoteDataSource: ForecastRemoteDataSource

    @MockK
    private lateinit var mockForecastLocalDataSource: ForecastLocalDataSource

    @Before
    override fun setUp() {
        super.setUp()

        repository = ForecastRepository(mockForecastRemoteDataSource, mockForecastLocalDataSource)
    }

    @Test
    fun `loadForecastByCoord should get current weather from the local data source if fetchRequired is false`() {
        val forecastLiveData: MutableLiveData<ForecastEntity> = MutableLiveData()
        forecastLiveData.postValue(createSampleForecastEntity(1, "Johannesburg"))
        val mockedObserver: Observer<Resource<ForecastEntity>> = mockk(relaxUnitFun = true)

        every { mockForecastLocalDataSource.getForecast() } returns forecastLiveData

        repository.loadForecastByCoord(lat, lon, false, Constants.Coords.METRIC).observeForever(mockedObserver)

        verify { mockForecastRemoteDataSource.getForecastByGeoCords(lat, lon, Constants.Coords.METRIC) wasNot called }
        verify { mockForecastLocalDataSource.getForecast() }
    }

    @Test
    fun `loadForecastByCoord should return valid data from the local data source if fetchRequired is false`() {
        val forecastLiveData: MutableLiveData<ForecastEntity> = MutableLiveData()
        forecastLiveData.postValue(createSampleForecastEntity(1, "Johannesburg"))
        val mockedObserver: Observer<Resource<ForecastEntity>> = mockk(relaxUnitFun = true)

        every { mockForecastLocalDataSource.getForecast() } returns forecastLiveData

        repository.loadForecastByCoord(lat, lon, false, Constants.Coords.METRIC).observeForever(mockedObserver)

        val forecastEntities = mutableListOf<Resource<ForecastEntity>>()
        verify { mockedObserver.onChanged(capture(forecastEntities)) }
        val forecastEntity = forecastEntities.first()

        assertEquals(Status.SUCCESS, forecastEntity.status)

        assertEquals("Johannesburg", forecastEntity.data?.city?.cityName)
        assertEquals(1, forecastEntity.data?.id)
    }

    @Test
    fun `loadCurrentWeatherByGeoCords should get current weather from the remote data source if fetchRequired is true`() {
        val forecastLiveData: MutableLiveData<ForecastEntity> = MutableLiveData()
        forecastLiveData.postValue(createSampleForecastEntity(1, "Johannesburg"))
        val mockedObserver: Observer<Resource<ForecastEntity>> = mockk(relaxUnitFun = true)

        every { mockForecastLocalDataSource.getForecast() } returns forecastLiveData
        every { mockForecastRemoteDataSource.getForecastByGeoCords(lat, lon, Constants.Coords.METRIC) } returns
                Single.just(createSampleForecastResponse())

        repository.loadForecastByCoord(lat, lon, true, Constants.Coords.METRIC).observeForever(mockedObserver)

        verify { mockForecastRemoteDataSource.getForecastByGeoCords(lat, lon, Constants.Coords.METRIC) }
        verify { mockForecastLocalDataSource.getForecast() wasNot called }
    }

    @Test
    fun `loadForecastByCoord should return valid data from the remote data source if fetchRequired is true`() {
        val forecastLiveData: MutableLiveData<ForecastEntity> = MutableLiveData()
        forecastLiveData.postValue(createSampleForecastEntity(1, "Johannesburg"))
        val mockedObserver: Observer<Resource<ForecastEntity>> = mockk(relaxUnitFun = true)

        every { mockForecastLocalDataSource.getForecast() } returns forecastLiveData
        every { mockForecastRemoteDataSource.getForecastByGeoCords(lat, lon, Constants.Coords.METRIC) } returns
                Single.just(createSampleForecastResponse())

        repository.loadForecastByCoord(lat, lon, true, Constants.Coords.METRIC).observeForever(mockedObserver)

        val forecastEntities = mutableListOf<Resource<ForecastEntity>>()
        verify { mockedObserver.onChanged(capture(forecastEntities)) }
        val forecastEntity = forecastEntities.first()

        assertEquals("Johannesburg", forecastEntity.data?.city?.cityName)
        assertEquals(1, forecastEntity.data?.id)
    }

    companion object {
        private const val lat = 30.0
        private const val lon = 34.0
    }

}
