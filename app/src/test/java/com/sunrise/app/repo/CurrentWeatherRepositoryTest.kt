package com.sunrise.app.repo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sunrise.app.BaseTest
import com.sunrise.app.core.Constants.Coords.METRIC
import com.sunrise.app.db.entity.CurrentWeatherEntity
import com.sunrise.app.domain.datasource.currentweather.CurrentWeatherLocalDataSource
import com.sunrise.app.domain.datasource.currentweather.CurrentWeatherRemoteDataSource
import com.sunrise.app.utils.createSampleCurrentWeatherEntity
import com.sunrise.app.utils.createSampleCurrentWeatherResponse
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

class CurrentWeatherRepositoryTest : BaseTest() {

    private lateinit var repository: CurrentWeatherRepository

    @MockK
    private lateinit var mockCurrentWeatherRemoteDataSource: CurrentWeatherRemoteDataSource

    @MockK
    private lateinit var mockCurrentWeatherLocalDataSource: CurrentWeatherLocalDataSource

    @Before
    override fun setUp() {
        super.setUp()

        repository = CurrentWeatherRepository(mockCurrentWeatherRemoteDataSource, mockCurrentWeatherLocalDataSource)
    }

    @Test
    fun `loadCurrentWeatherByGeoCords should get current weather from the local data source if fetchRequired is false`() {
        val currentWeatherLiveData: MutableLiveData<CurrentWeatherEntity> = MutableLiveData()
        currentWeatherLiveData.postValue(createSampleCurrentWeatherEntity("Johannesburg", 1))
        val mockedObserver: Observer<Resource<CurrentWeatherEntity>> = mockk(relaxUnitFun = true)

        every { mockCurrentWeatherLocalDataSource.getCurrentWeather() } returns currentWeatherLiveData

        repository.loadCurrentWeatherByGeoCords(lat, lon, false, METRIC).observeForever(mockedObserver)

        verify { mockCurrentWeatherRemoteDataSource.getCurrentWeatherByGeoCords(lat, lon, METRIC) wasNot called }
        verify { mockCurrentWeatherLocalDataSource.getCurrentWeather() }
    }

    @Test
    fun `loadCurrentWeatherByGeoCords should return valid data from the local data source if fetchRequired is false`() {
        val currentWeatherLiveData: MutableLiveData<CurrentWeatherEntity> = MutableLiveData()
        currentWeatherLiveData.postValue(createSampleCurrentWeatherEntity("Johannesburg", 1))
        val mockedObserver: Observer<Resource<CurrentWeatherEntity>> = mockk(relaxUnitFun = true)

        every { mockCurrentWeatherLocalDataSource.getCurrentWeather() } returns currentWeatherLiveData

        repository.loadCurrentWeatherByGeoCords(lat, lon, false, METRIC).observeForever(mockedObserver)

        val currentWeatherEntities = mutableListOf<Resource<CurrentWeatherEntity>>()
        verify { mockedObserver.onChanged(capture(currentWeatherEntities)) }
        val currentWeatherEntity = currentWeatherEntities.first()

        assertEquals(Status.SUCCESS, currentWeatherEntity.status)

        assertEquals("Johannesburg", currentWeatherEntity.data?.name)
        assertEquals(1, currentWeatherEntity.data?.id)
    }

    @Test
    fun `loadCurrentWeatherByGeoCords should get current weather from the remote data source if fetchRequired is true`() {
        val currentWeatherLiveData: MutableLiveData<CurrentWeatherEntity> = MutableLiveData()
        currentWeatherLiveData.postValue(createSampleCurrentWeatherEntity("Johannesburg", 1))
        val mockedObserver: Observer<Resource<CurrentWeatherEntity>> = mockk(relaxUnitFun = true)

        every { mockCurrentWeatherLocalDataSource.getCurrentWeather() } returns currentWeatherLiveData
        every { mockCurrentWeatherRemoteDataSource.getCurrentWeatherByGeoCords(lat, lon, METRIC) } returns
                Single.just(createSampleCurrentWeatherResponse())

        repository.loadCurrentWeatherByGeoCords(lat, lon, true, METRIC).observeForever(mockedObserver)

        verify { mockCurrentWeatherRemoteDataSource.getCurrentWeatherByGeoCords(lat, lon, METRIC) }
        verify { mockCurrentWeatherLocalDataSource.getCurrentWeather() wasNot called }
    }

    @Test
    fun `loadCurrentWeatherByGeoCords should return valid data from the remote data source if fetchRequired is true`() {
        val currentWeatherLiveData: MutableLiveData<CurrentWeatherEntity> = MutableLiveData()
        currentWeatherLiveData.postValue(createSampleCurrentWeatherEntity("Johannesburg", 1))
        val mockedObserver: Observer<Resource<CurrentWeatherEntity>> = mockk(relaxUnitFun = true)

        every { mockCurrentWeatherLocalDataSource.getCurrentWeather() } returns currentWeatherLiveData
        every { mockCurrentWeatherRemoteDataSource.getCurrentWeatherByGeoCords(lat, lon, METRIC) } returns
                Single.just(createSampleCurrentWeatherResponse())

        repository.loadCurrentWeatherByGeoCords(lat, lon, true, METRIC).observeForever(mockedObserver)

        val currentWeatherEntities = mutableListOf<Resource<CurrentWeatherEntity>>()
        verify { mockedObserver.onChanged(capture(currentWeatherEntities)) }

        val currentWeatherEntity = currentWeatherEntities.first()

        assertEquals("Johannesburg", currentWeatherEntity.data?.name)
        assertEquals(1, currentWeatherEntity.data?.id)
    }

    companion object {
        private const val lat = 30.0
        private const val lon = 34.0
    }

}
