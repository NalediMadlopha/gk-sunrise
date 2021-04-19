package com.sunrise.app.ui.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sunrise.app.R
import com.sunrise.app.core.Constants
import com.sunrise.app.databinding.WeatherFragmentBinding
import com.sunrise.app.di.Injectable
import com.sunrise.app.domain.usercase.CurrentWeatherUseCase
import com.sunrise.app.domain.usercase.ForecastUseCase
import com.sunrise.app.utils.domain.Status
import com.sunrise.app.utils.extensions.isNetworkAvailable
import dagger.android.AndroidInjection
import javax.inject.Inject


class WeatherFragment : Fragment(), Injectable, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: WeatherFragmentBinding
    private val viewModel: WeatherViewModel by viewModels { viewModelFactory }
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(activity)
        super.onCreate(savedInstanceState)
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel?.getCurrentWeatherViewState()?.observe(viewLifecycleOwner, {
            binding.currentWeatherDay.viewState = it
        })

        binding.viewModel?.getForecastViewState()?.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = it.isLoading()

            if (it.status == Status.SUCCESS && it.data != null) {
                binding.toolbar.toolbarTitle.text = it.data.city?.getCityAndCountry()
                binding.forecastRecyclerview.adapter = ForecastAdapter(it.data.list)
                binding.swipeRefresh.isRefreshing = it.isLoading()

                // TODO: Investigate why visibility does not work with when using the view state status with  data-binding
                binding.currentWeatherDay.currentWeatherDayMaterialCardView.visibility = View.VISIBLE
                binding.forecastRecyclerview.visibility = View.VISIBLE
                binding.emptyState.visibility = View.GONE
            } else if ((it.status == Status.ERROR || it.status == Status.SUCCESS) && it.data == null) {
                binding.currentWeatherDay.currentWeatherDayMaterialCardView.visibility = View.GONE
                binding.forecastRecyclerview.visibility = View.GONE
                binding.emptyState.visibility = View.VISIBLE
            }
        })
    }

    override fun onRefresh() {
        fetchWeatherDetails()
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun checkPermissions() {
        val isPermissionGranted = context?.let { ContextCompat.checkSelfPermission(it, ACCESS_FINE_LOCATION)}
        if (isPermissionGranted != PackageManager.PERMISSION_GRANTED) {
            requestMultiplePermissions.launch(arrayOf(ACCESS_FINE_LOCATION))
        } else {
            fetchWeatherDetails()
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[ACCESS_FINE_LOCATION] == true) {
                fetchWeatherDetails()
            } else {
                Toast.makeText(context, getString(R.string.permission_not_granted_message), Toast.LENGTH_LONG).show()
                requireActivity().finish()
            }
        }

    @SuppressLint("MissingPermission")
    private fun fetchWeatherDetails() {
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
            binding.viewModel?.setCoordinates(location.latitude, location.longitude)
        }

        val lat: String? =  binding.viewModel?.sharedPreferences?.getString(Constants.Coords.LAT, "")
        val lon: String? = binding.viewModel?.sharedPreferences?.getString(Constants.Coords.LON, "")

        if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
            binding.viewModel?.setCurrentWeatherParams(
                CurrentWeatherUseCase.CurrentWeatherParams(lat, lon, requireContext().isNetworkAvailable(), Constants.Coords.METRIC)
            )

            binding.viewModel?.setForecastParams(
                ForecastUseCase.ForecastParams(lat, lon, requireContext().isNetworkAvailable(), Constants.Coords.METRIC)
            )
        }
    }

}