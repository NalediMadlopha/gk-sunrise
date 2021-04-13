package com.sunrise.app.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sunrise.app.BaseTest
import com.sunrise.app.domain.usercase.CurrentWeatherUseCase
import com.sunrise.app.domain.usercase.ForecastUseCase
import com.sunrise.app.utils.domain.Status
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainFragmentViewModelTest : BaseTest() {

    private lateinit var fragmentViewModel: MainFragmentViewModel

    @MockK
    private lateinit var mockCurrentWeatherUseCase: CurrentWeatherUseCase

    @MockK
    private lateinit var mockForecastUseCase: ForecastUseCase

    @Before
    override fun setUp() {
        super.setUp()
        fragmentViewModel = MainFragmentViewModel(mockCurrentWeatherUseCase, mockForecastUseCase)
    }

    @Test
    fun `setCurrentWeatherParams should set the view state to loading when use case returns an loading status`() {
        val viewStateObserver: Observer<CurrentWeatherViewState> = mockk(relaxUnitFun = true)
        val viewStateLiveData = MutableLiveData<CurrentWeatherViewState>().apply {
            postValue(CurrentWeatherViewState(Status.LOADING, null, null))
        }
        fragmentViewModel.getCurrentWeatherViewState().observeForever(viewStateObserver)
        every { mockCurrentWeatherUseCase.execute(any()) } returns viewStateLiveData

        fragmentViewModel.setCurrentWeatherParams(CurrentWeatherUseCase.CurrentWeatherParams("30.0", "20.0", true, "metric"))

        val currentWeatherViewStateSlots = mutableListOf<CurrentWeatherViewState>()
        verify { viewStateObserver.onChanged(capture(currentWeatherViewStateSlots)) }
        val state = currentWeatherViewStateSlots.first()

        assertEquals(Status.LOADING, state.status)
    }

    @Test
    fun `setCurrentWeatherParams should set the view state to error when use case returns an error status`() {
        val viewStateObserver: Observer<CurrentWeatherViewState> = mockk(relaxUnitFun = true)
        val viewStateLiveData = MutableLiveData<CurrentWeatherViewState>().apply {
            postValue(CurrentWeatherViewState(Status.ERROR, null, null))
        }
        fragmentViewModel.getCurrentWeatherViewState().observeForever(viewStateObserver)
        every { mockCurrentWeatherUseCase.execute(any()) } returns viewStateLiveData

        fragmentViewModel.setCurrentWeatherParams(CurrentWeatherUseCase.CurrentWeatherParams("30.0", "20.0", true, "metric"))

        val currentWeatherViewStateSlots = mutableListOf<CurrentWeatherViewState>()
        verify { viewStateObserver.onChanged(capture(currentWeatherViewStateSlots)) }
        val viewState = currentWeatherViewStateSlots.first()

        assertEquals(Status.ERROR, viewState.status)
    }

    @Test
    fun `setCurrentWeatherParams should set the view state to success when use case returns an success status`() {
        val viewStateObserver: Observer<CurrentWeatherViewState> = mockk(relaxUnitFun = true)
        val viewStateLiveData = MutableLiveData<CurrentWeatherViewState>().apply {
            postValue(CurrentWeatherViewState(Status.SUCCESS, null, null))
        }
        fragmentViewModel.getCurrentWeatherViewState().observeForever(viewStateObserver)
        every { mockCurrentWeatherUseCase.execute(any()) } returns viewStateLiveData

        fragmentViewModel.setCurrentWeatherParams(CurrentWeatherUseCase.CurrentWeatherParams("30.0", "20.0", true, "metric"))

        val currentWeatherViewStateList = mutableListOf<CurrentWeatherViewState>()
        verify { viewStateObserver.onChanged(capture(currentWeatherViewStateList)) }
        val viewState = currentWeatherViewStateList.first()

        assertEquals(Status.SUCCESS, viewState.status)
    }

    @Test
    fun `setForecastParams should set the view state to loading when use case returns an loading status`() {
        val viewStateObserver: Observer<ForecastViewState> = mockk(relaxUnitFun = true)
        val viewStateLiveData = MutableLiveData<ForecastViewState>().apply {
            postValue(ForecastViewState(Status.LOADING, null, null))
        }
        fragmentViewModel.getForecastViewState().observeForever(viewStateObserver)
        every { mockForecastUseCase.execute(any()) } returns viewStateLiveData

        fragmentViewModel.setForecastParams(ForecastUseCase.ForecastParams("30.0", "20.0", true, "metric"))

        val forecastViewStateList = mutableListOf<ForecastViewState>()
        verify { viewStateObserver.onChanged(capture(forecastViewStateList)) }
        val state = forecastViewStateList.first()

        assertEquals(Status.LOADING, state.status)
    }

    @Test
    fun `setForecastParams should set the view state to error when use case returns an error status`() {
        val viewStateObserver: Observer<ForecastViewState> = mockk(relaxUnitFun = true)
        val viewStateLiveData = MutableLiveData<ForecastViewState>().apply {
            postValue(ForecastViewState(Status.ERROR, null, null))
        }
        fragmentViewModel.getForecastViewState().observeForever(viewStateObserver)
        every { mockForecastUseCase.execute(any()) } returns viewStateLiveData

        fragmentViewModel.setForecastParams(ForecastUseCase.ForecastParams("30.0", "20.0", true, "metric"))

        val forecastViewStateList = mutableListOf<ForecastViewState>()
        verify { viewStateObserver.onChanged(capture(forecastViewStateList)) }
        val state = forecastViewStateList.first()

        assertEquals(Status.ERROR, state.status)
    }

    @Test
    fun `setForecastParams should set the view state to success when use case returns an success status`() {
        val viewStateObserver: Observer<ForecastViewState> = mockk(relaxUnitFun = true)
        val viewStateLiveData = MutableLiveData<ForecastViewState>().apply {
            postValue(ForecastViewState(Status.SUCCESS, null, null))
        }
        fragmentViewModel.getForecastViewState().observeForever(viewStateObserver)
        every { mockForecastUseCase.execute(any()) } returns viewStateLiveData

        fragmentViewModel.setForecastParams(ForecastUseCase.ForecastParams("30.0", "20.0", true, "metric"))

        val forecastViewStateList = mutableListOf<ForecastViewState>()
        verify { viewStateObserver.onChanged(capture(forecastViewStateList)) }
        val state = forecastViewStateList.first()

        assertEquals(Status.SUCCESS, state.status)
    }

}
