package com.sunrise.app.domain.usercase

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.sunrise.app.core.Constants
import com.sunrise.app.db.entity.ForecastEntity
import com.sunrise.app.repo.ForecastRepository
import com.sunrise.app.ui.main.ForecastViewState
import com.sunrise.app.utils.UseCaseLiveData
import com.sunrise.app.utils.domain.Resource
import com.sunrise.app.utils.mapper.ForecastMapper
import javax.inject.Inject

class ForecastUseCase @Inject internal constructor(private val repository: ForecastRepository) : UseCaseLiveData<ForecastViewState, ForecastUseCase.ForecastParams, ForecastRepository>() {

    override fun getRepository(): ForecastRepository {
        return repository
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun buildUseCaseObservable(params: ForecastParams?): LiveData<ForecastViewState> {
        return repository.loadForecastByCoord(
            params?.lat?.toDouble() ?: 0.0,
            params?.lon?.toDouble() ?: 0.0,
            params?.fetchRequired
                ?: false,
            units = params?.units ?: Constants.Coords.METRIC
        ).map {
            onForecastResultReady(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onForecastResultReady(resource: Resource<ForecastEntity>): ForecastViewState {
        val mappedList = resource.data?.list?.let { ForecastMapper().mapFrom(it) }
        resource.data?.list = mappedList

        return ForecastViewState(
            status = resource.status,
            error = resource.message,
            data = resource.data
        )
    }

    class ForecastParams(
        val lat: String = "",
        val lon: String = "",
        val fetchRequired: Boolean,
        val units: String
    ) : Params()
}